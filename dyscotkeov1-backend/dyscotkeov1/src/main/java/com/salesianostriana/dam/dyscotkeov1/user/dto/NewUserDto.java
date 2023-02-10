package com.salesianostriana.dam.dyscotkeov1.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserDto {

    private String username;
    private String password;
    private String verifyPassword;
    private String avatar;
    private String fullName;
    private LocalDateTime createdAt = LocalDateTime.now();

}
