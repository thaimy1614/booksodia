package com.begin.bg.services;

import com.begin.bg.dto.mail.SendOtp;
import com.begin.bg.dto.mail.SendPassword;
import com.begin.bg.dto.request.ChangePasswordRequest;
import com.begin.bg.dto.request.CheckOTPRequest;
import com.begin.bg.dto.request.RefreshTokenRequest;
import com.begin.bg.dto.request.SendOTPRequest;
import com.begin.bg.dto.response.ChangePasswordResponse;
import com.begin.bg.dto.response.CheckOTPResponse;
import com.begin.bg.dto.response.IntrospectResponse;
import com.begin.bg.dto.response.SendOTPResponse;
import com.begin.bg.entities.InvalidatedToken;
import com.begin.bg.entities.ResponseObject;
import com.begin.bg.entities.User;
import com.begin.bg.repositories.InvalidatedTokenRepository;
import com.begin.bg.repositories.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${jwt.signer-key}")
    private String KEY;
    @Value("${jwt.expiration-duration}")
    private long EXPIRATION_DURATION;
    @Value("${jwt.refreshable-duration}")
    private String REFRESHABLE_DURATION;

    public ResponseObject authenticate(User user) throws Exception {
        User authUser = userRepository.findByEmail(user.getEmail()).get();
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
                .subject(user.getEmail())
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

        InvalidatedToken invalidatedToken = InvalidatedToken
                .builder()
                .id(jId)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws Exception {
        JWSVerifier verifier = new MACVerifier(KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = (isRefresh) ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(Long.parseLong(REFRESHABLE_DURATION), ChronoUnit.SECONDS).toEpochMilli()) : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if (!(verified&& expiryTime.after(new Date()))) {
            throw new Exception("UNAUTHENTICATED");
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
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

        InvalidatedToken invalidatedToken = InvalidatedToken
                .builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByEmail(username);

        return generateToken(user.get());
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
        redisTemplate.opsForValue().set(email,OTP,1, TimeUnit.MINUTES);
        // Send OTP via mail - mail-service
        SendOtp sendOtp = SendOtp.builder().email(email).otp(OTP).topic("OTP - FORGET PASSWORD").build();
        kafkaTemplate.send("sendOtp", sendOtp);
        return SendOTPResponse.builder().isSent(true).build();
    }

    public CheckOTPResponse checkOTP(String otp, String email) {
        if(otp.equals(redisTemplate.opsForValue().get(email))){
            // delete old OTP
            redisTemplate.delete(email);
            String newPassword = generate();
            var user = userRepository.findByEmail(email).get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            // send new password
            SendPassword sendPassword = SendPassword.builder().email(email).password(newPassword).topic("New Password - FORGET PASSWORD").build();
            kafkaTemplate.send("sendNewPassword", sendPassword);
            return CheckOTPResponse.builder().isValid(true).build();
        }else{
            return CheckOTPResponse.builder().isValid(false).build();
        }
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        var account = SecurityContextHolder.getContext().getAuthentication();
        String email = account.getName();
        var accountInfo = userRepository.findByEmail(email).get();
        if(passwordEncoder.matches(request.getOldPassword(),accountInfo.getPassword())){
            accountInfo.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(accountInfo);
            return ChangePasswordResponse.builder().success(true).build();
        }else {
            return ChangePasswordResponse.builder().success(false).build();
        }
    }

    private String generate(){
        int OTP = new Random().nextInt(900000) + 100000;
        return String.valueOf(OTP);
    }
}


