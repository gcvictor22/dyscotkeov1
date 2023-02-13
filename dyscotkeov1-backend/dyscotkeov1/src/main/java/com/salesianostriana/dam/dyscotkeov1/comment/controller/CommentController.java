package com.salesianostriana.dam.dyscotkeov1.comment.controller;

import com.salesianostriana.dam.dyscotkeov1.comment.dto.NewCommentDto;
import com.salesianostriana.dam.dyscotkeov1.comment.model.Comment;
import com.salesianostriana.dam.dyscotkeov1.comment.service.CommentService;
import com.salesianostriana.dam.dyscotkeov1.post.dto.ViewPostDto;
import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{id}")
    public ResponseEntity<ViewPostDto> create(@Valid @RequestBody NewCommentDto newCommentDto, @PathVariable Long id, @AuthenticationPrincipal User user){
        Comment created = commentService.save(newCommentDto);

        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(createdURI).body(commentService.responseComment(created, id, user));
    }

    @PutMapping("/{id}")
    public ViewPostDto edit(@Valid @RequestBody NewCommentDto newCommentDto, @PathVariable Long id){

        Post post = commentService.edit(newCommentDto, id);

        return ViewPostDto.of(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return commentService.delete(id);
    }

}
