package com.begin.bg.services;

import com.begin.bg.dto.mail.SendOtp;
import com.begin.bg.dto.mail.SendPassword;
import com.begin.bg.dto.request.*;
import com.begin.bg.dto.response.*;
import com.begin.bg.entities.ResponseObject;
import com.begin.bg.entities.Role;
import com.begin.bg.entities.User;
import com.begin.bg.enums.UserRole;
import com.begin.bg.enums.UserStatus;
import com.begin.bg.repositories.UserRepository;
import com.begin.bg.repositories.httpclient.OutboundIdentityClient;
import com.begin.bg.repositories.httpclient.OutboundUserClient;
import com.begin.bg.repositories.httpclient.ProfileClient;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    private final ProfileClient profileClient;
    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;
    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;
    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;
    @Value("${jwt.signer-key}")
    private String KEY;
    @Value("${jwt.expiration-duration}")
    private long EXPIRATION_DURATION;
    @Value("${jwt.refreshable-duration}")
    private String REFRESHABLE_DURATION;

    public ResponseObject authenticate(User user) throws Exception {
        log.info(user.getEmail() + " " + user.getPassword());
        User authUser = userRepository.findByEmail(user.getEmail()).orElseThrow();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean check = passwordEncoder.matches(user.getPassword(), authUser.getPassword());
        if (!check) {
            return null;
        }
        var token = generateToken(authUser);
        return ResponseObject
                .builder()
                .status("OK")
                .message("Login successful!")
                .data(token)
                .build();

    }

    private String generateToken(User user) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet
                .Builder()
                .subject(user.getId())
                .issuer("Thaidq")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(EXPIRATION_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        jwsObject.sign(new MACSigner(KEY));
        return jwsObject.serialize();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!user.getRoles().isEmpty()) {
            user.getRoles().forEach(role ->
                    {
                        stringJoiner.add(role.getName());
                        if (!role.getPermissions().isEmpty())
                            role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                    }
            );
        }
        return stringJoiner.toString();
    }

    public void logout(String token) throws Exception {
        var signedJWT = verifyToken(token, true);
        String jId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        addTokenToBlacklist(jId, expiryTime);
    }

    private boolean isTokenInBlacklist(String jwtId) {
        return redisTemplate.opsForValue().get("bl_" + jwtId) != null;
    }

    private void addTokenToBlacklist(String jwtId, Date expiryTime) {
        redisTemplate.opsForValue().set("bl_" + jwtId, jwtId);
        redisTemplate.expireAt("bl_" + jwtId, expiryTime);
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws Exception {
        JWSVerifier verifier = new MACVerifier(KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = (isRefresh) ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(Long.parseLong(REFRESHABLE_DURATION), ChronoUnit.SECONDS).toEpochMilli()) : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) {
            throw new Exception("UNAUTHENTICATED");
        }

        if (isTokenInBlacklist(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new Exception("UNAUTHENTICATED");
        }
        return signedJWT;
    }

    public IntrospectResponse introspect(String token) throws Exception {
        boolean isValid = true;
        try {
            verifyToken(token, false);

        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse
                .builder()
                .valid(isValid)
                .build();
    }

    public String refreshToken(RefreshTokenRequest request) throws Exception {
        var signedJWT = verifyToken(request.getToken(), true);
        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        addTokenToBlacklist(jit, expiryTime);

        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByEmail(username).orElseThrow();

        return generateToken(user);
    }

    public AuthenticationResponse outboundAuthenticate(String code) throws JOSEException {
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());
        log.info("TOKEN RESPONSE {}", response);

        // Get user info
        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        log.info("User Info {}", userInfo);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().name(UserRole.ADMIN.name()).build());

        // Onboard user
        var user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(
                () -> {
                    var profileRequest = ProfileCreationRequest.builder()
                            .userId(userInfo.getEmail())
                            .fullName(userInfo.getGivenName() + userInfo.getFamilyName())
                            .image(userInfo.getPicture())
                            .build();
                    // call profile service to add profile
                    profileClient.createProfile(profileRequest);
                    return userRepository.save(User.builder()
                            .email(userInfo.getEmail())
                            .roles(roles)
                            .status(UserStatus.ACTIVATED)
                            .build());
                });
        // Generate token
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public VerifyAccountResponse verifyAccount(String email, String token) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getStatus() != UserStatus.UNVERIFIED) {
            return VerifyAccountResponse.builder().success(false).build();
        }
        if (token.equals(redisTemplate.opsForValue().get(email + "_verify"))) {
            redisTemplate.delete(email + "_verify");
            user.setStatus(UserStatus.ACTIVATED);
            userRepository.save(user);
            return VerifyAccountResponse.builder().success(true).build();
        }
        return VerifyAccountResponse.builder().success(false).build();
    }


    public SendOTPResponse sendOTPForForgetPassword(SendOTPRequest request) {
        String email = request.getEmail();
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return SendOTPResponse.builder().isSent(false).build();
        }
        // Generate OTP and store in redis
        String OTP = generate();
        // Store OTP in redis
        redisTemplate.opsForValue().set(email, OTP, 1, TimeUnit.MINUTES);
        // Send OTP via mail - mail-service
        SendOtp sendOtp = SendOtp.builder().email(email).otp(OTP).topic("OTP - FORGET PASSWORD").build();
        kafkaTemplate.send("sendOtp", sendOtp);
        return SendOTPResponse.builder().isSent(true).build();
    }

    public CheckOTPResponse checkOTP(String otp, String email) {
        if (otp.equals(redisTemplate.opsForValue().get(email))) {
            // delete old OTP
            redisTemplate.delete(email);
            String newPassword = generate();
            var user = userRepository.findByEmail(email).orElseThrow();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            // send new password
            SendPassword sendPassword = SendPassword.builder().email(email).password(newPassword).topic("New Password - FORGET PASSWORD").build();
            kafkaTemplate.send("sendNewPassword", sendPassword);
            return CheckOTPResponse.builder().isValid(true).build();
        } else {
            return CheckOTPResponse.builder().isValid(false).build();
        }
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        var account = SecurityContextHolder.getContext().getAuthentication();
        String email = account.getName();
        var accountInfo = userRepository.findByEmail(email).get();
        if (passwordEncoder.matches(request.getOldPassword(), accountInfo.getPassword())) {
            accountInfo.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(accountInfo);
            return ChangePasswordResponse.builder().success(true).build();
        } else {
            return ChangePasswordResponse.builder().success(false).build();
        }
    }

    private String generate() {
        int OTP = new Random().nextInt(900000) + 100000;
        return String.valueOf(OTP);
    }

    // Get url of this service
    private UriComponentsBuilder getThisServiceURL() {
        HttpServletRequest currRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        return UriComponentsBuilder
                .fromHttpUrl(UrlUtils.buildFullRequestUrl(currRequest))
                .replacePath(currRequest.getContextPath())
                .replaceQuery(null)
                .fragment(null);
    }
}


