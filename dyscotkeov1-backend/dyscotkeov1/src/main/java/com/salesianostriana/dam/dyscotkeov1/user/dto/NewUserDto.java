package com.salesianostriana.dam.dyscotkeov1.user.dto;

import com.salesianostriana.dam.dyscotkeov1.validation.annotation.FieldsValueMatch;
import com.salesianostriana.dam.dyscotkeov1.validation.annotation.StrongPassword;
import com.salesianostriana.dam.dyscotkeov1.validation.annotation.UniqueUserName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldsValueMatch(
        field = "password", fieldMatch = "verifyPassword",
        message = "{newUserDto.password.nomatch}"
)
public class NewUserDto {

    @NotEmpty(message = "{newUserDto.fullname.notempty}")
    private String fullName;

    @NotEmpty(message = "{newUserDto.username.notempty}")
    @UniqueUserName(message = "{newUserDto.username.unique}")
    private String username;

    @NotEmpty(message = "{newUserDto.password.notempty}")
    @StrongPassword
    private String password;

    @NotEmpty(message = "{newUserDto.verifypassword.notempty}")
    private String verifyPassword;

    @NotEmpty(message = "{newUserDto.email.notempty}")
    @Email(message = "{newUserDto.email.email}")
    private String email;

    @NotEmpty(message = "{newUserDto.phone.notEmpty}")
    private String phoneNumber;

    @URL(message = "{newUserDto.email.email}")
    private String imgPath;

    private LocalDateTime createdAt = LocalDateTime.now();

}
