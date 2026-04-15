package com.dunnas.tms.feature.user.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserAccountRequestDto(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must have at most 100 characters")
        String name,

        @NotBlank(message = "Username is required")
        @Size(max = 100, message = "Username must have at most 100 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(max = 100, message = "Password must have at most 100 characters")
        String password,

        @NotBlank(message = "Role is required")
        @Pattern(
                regexp = "^(ADMIN|RESIDENT|COLLABORATOR|ROLE_ADMIN|ROLE_RESIDENT|ROLE_COLLABORATOR)$",
                message = "Role must be ADMIN, RESIDENT, COLLABORATOR or ROLE_* equivalent"
        )
        String role,

        // tamanhos arbitrários para evitar problemas de performance
        @Size(max = 200, message = "User can be linked to at most 200 units")
        Set<Long> unitIds,

        // tamanhos arbitrários para evitar problemas de performance
        @Size(max = 200, message = "User can be linked to at most 200 collaborator ticket types")
        Set<Long> collaboratorTicketTypeIds
) {
}
