package com.mubeen.mubeenmart.dto;

import com.mubeen.mubeenmart.model.Role;
import com.mubeen.mubeenmart.model.User;

import java.time.LocalDateTime;

/**
 * Public DTO representing user account without sensitive security fields (e.g. passwordHash).
 */
public class UserResponseDTO {

    private final Long id;
    private final String name;
    private final String email;
    private final Role role;
    private final LocalDateTime createdAt;

    public UserResponseDTO(Long id, String name, String email, Role role, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static UserResponseDTO fromEntity(User user) {
        if (user == null) return null;
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
