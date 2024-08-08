package com.thai.profile.dto.mail;

import com.thai.profile.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String email;
    private String name;
    private String reason;
    private String topic;

    public static UserInfo.Builder createUserInfo(User user) {
        return UserInfo.builder()
                .email(user.getEmail())
                .name(user.getFullName());
    }
}
