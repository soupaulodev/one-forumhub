package br.com.soupaulodev.forumhub.modules.user.controller.dto;

import br.com.soupaulodev.forumhub.modules.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Data
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private UserRole role;
    private Instant createdAt;
    private Instant updatedAt;
}
