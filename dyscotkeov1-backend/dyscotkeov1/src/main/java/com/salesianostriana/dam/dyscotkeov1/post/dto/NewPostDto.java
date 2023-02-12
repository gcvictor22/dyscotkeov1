package com.salesianostriana.dam.dyscotkeov1.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPostDto {

    private String affair;

    @NotEmpty(message = "{newPostDto.content.notEmpty}")
    private String content;

    private String imgPath;

}
