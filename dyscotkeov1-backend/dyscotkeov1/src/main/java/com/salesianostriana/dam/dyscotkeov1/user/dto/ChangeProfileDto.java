package com.salesianostriana.dam.dyscotkeov1.user.dto;

import com.salesianostriana.dam.dyscotkeov1.validation.annotation.StrongPassword;
import com.salesianostriana.dam.dyscotkeov1.validation.annotation.UniqueEmail;
import com.salesianostriana.dam.dyscotkeov1.validation.annotation.UniquePhoneNumber;
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
public class ChangeProfileDto {

    @NotEmpty(message = "{newUserDto.fullname.notempty}")
    private String fullName;

    @NotEmpty(message = "{newUserDto.username.notempty}")
    @UniqueUserName(message = "{newUserDto.username.unique}")
    private String username;

    @NotEmpty(message = "{newUserDto.email.notempty}")
    @Email(message = "{newUserDto.email.email}")
    @UniqueEmail(message = "{newUserDto.email.unique}")
    private String email;

    @NotEmpty(message = "{newUserDto.phone.notEmpty}")
    @UniquePhoneNumber(message = "{newUserDto.phone.unique}")
    private String phoneNumber;

    @URL(message = "{newUserDto.imgPath.url}")
    private String imgPath;

}
