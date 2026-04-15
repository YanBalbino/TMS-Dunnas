package com.dunnas.tms.feature.user;

import java.util.Locale;

import com.dunnas.tms.feature.user.dto.UserAccountRequestDto;

public final class UserAccountMapper {

    private UserAccountMapper() {
    }

    public static UserAccount toEntity(UserAccountRequestDto request) {
        return UserAccount.builder()
                .name(request.name())
                .username(request.username())
                .password(request.password())
                .role(normalizeRole(request.role()))
                .build();
    }

    public static void updateEntity(UserAccount userAccount, UserAccountRequestDto request) {
        userAccount.setName(request.name());
        userAccount.setUsername(request.username());
        userAccount.setPassword(request.password());
        userAccount.setRole(normalizeRole(request.role()));
    }

    private static String normalizeRole(String role) {
        if (role == null) {
            return null;
        }

        // sempre uppercase e com prefixo ROLE_
        String upper = role.trim().toUpperCase(Locale.ROOT);
        return upper.startsWith("ROLE_") ? upper : "ROLE_" + upper;
    }
}
