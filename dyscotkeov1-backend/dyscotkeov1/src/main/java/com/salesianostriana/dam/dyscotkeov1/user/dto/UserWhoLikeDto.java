package com.salesianostriana.dam.dyscotkeov1.user.dto;

import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWhoLikeDto {

    private String userName;
    private String imgPath;
    private boolean verified;

    public static UserWhoLikeDto of(User user){
        return UserWhoLikeDto.builder()
                .userName(user.getUsername())
                .imgPath(user.getImgPath())
                .verified(user.isVerified())
                .build();
    }

}
