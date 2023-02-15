package com.salesianostriana.dam.dyscotkeov1.comment.service;

import com.salesianostriana.dam.dyscotkeov1.comment.dto.GetCommentDto;
import com.salesianostriana.dam.dyscotkeov1.comment.dto.NewCommentDto;
import com.salesianostriana.dam.dyscotkeov1.comment.model.Comment;
import com.salesianostriana.dam.dyscotkeov1.comment.repository.CommentRespository;
import com.salesianostriana.dam.dyscotkeov1.exception.accesdenied.CommentDeniedAccessException;
import com.salesianostriana.dam.dyscotkeov1.exception.accesdenied.PostAccessDeniedExeption;
import com.salesianostriana.dam.dyscotkeov1.exception.badrequest.CommentBadRequestToDeleteException;
import com.salesianostriana.dam.dyscotkeov1.exception.notfound.CommentNotFoundException;
import com.salesianostriana.dam.dyscotkeov1.exception.notfound.PostNotFoundException;
import com.salesianostriana.dam.dyscotkeov1.post.dto.ViewPostDto;
import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.post.repository.PostRepository;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import com.salesianostriana.dam.dyscotkeov1.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRespository commentRespository;
    private final PostRepository postRepository;

    public Comment save(NewCommentDto newCommentDto) {
        Comment comment = Comment.builder()
                .content(newCommentDto.getContent())
                .imgPath(newCommentDto.getImgPath())
                .build();

        return commentRespository.save(comment);
    }

    public ViewPostDto responseComment(Comment comment, Long id, User user){
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));

        comment.addPost(post);
        comment.addUser(user);
        postRepository.save(post);
        commentRespository.save(comment);

        return ViewPostDto.of(post);
    }

    public Post edit(NewCommentDto newCommentDto, Long id, User loggedUser) {

        Comment comment = commentRespository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
        Post post = postRepository.findById(comment.getCommentedPost().getId()).orElseThrow(() -> new PostNotFoundException(comment.getCommentedPost().getId()));

        if (loggedUser.getId() != comment.getUserWhoComment().getId()){
            throw new CommentDeniedAccessException();
        }
        if (!post.getComments().contains(comment)){
            throw new CommentNotFoundException(id);
        }
        commentRespository.findById(id).map(old -> {
            old.setContent(newCommentDto.getContent());
            old.setImgPath(newCommentDto.getImgPath());
            return commentRespository.save(old);
        });
        return post;
    }

    public ResponseEntity<?> delete(Long id, User loggedUser){
        Comment comment = commentRespository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
        Post post = postRepository.findById(comment.getCommentedPost().getId()).orElseThrow(() -> new PostNotFoundException(comment.getCommentedPost().getId()));

        if (loggedUser.getId() != comment.getUserWhoComment().getId()){
            throw new CommentDeniedAccessException();
        }
        if (!post.getComments().contains(comment)){
            throw new CommentBadRequestToDeleteException(id);
        }
        comment.removePost();
        commentRespository.delete(comment);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
