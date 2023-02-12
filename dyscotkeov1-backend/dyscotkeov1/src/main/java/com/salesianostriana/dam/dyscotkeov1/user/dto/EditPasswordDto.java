package com.salesianostriana.dam.dyscotkeov1.user.dto;

import com.salesianostriana.dam.dyscotkeov1.validation.annotation.user.DontFieldsValueMatch;
import com.salesianostriana.dam.dyscotkeov1.validation.annotation.user.FieldsValueMatch;
import com.salesianostriana.dam.dyscotkeov1.validation.annotation.user.StrongPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldsValueMatch(
        field = "newPassword", fieldMatch = "newPasswordVerify",
        message = "{newUserDto.password.nomatch}"
)
@DontFieldsValueMatch(
        field = "oldPassword", fieldMatch = "newPassword",
        message = "{changePasswordDto.password.match}"
)
public class EditPasswordDto {

    @NotEmpty(message = "{changePasswordDto.oldPassword.notEmpty}")
    private String oldPassword;

    @NotEmpty(message = "{changePasswordDto.newPassword.notEmpty}")
    @StrongPassword
    private String newPassword;

    @NotEmpty(message = "{changePasswordDto.verifypassword.notempty}")
    @StrongPassword
    private String newPasswordVerify;

}
