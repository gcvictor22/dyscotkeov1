package com.salesianostriana.dam.dyscotkeov1.comment.service;

import com.salesianostriana.dam.dyscotkeov1.comment.dto.GetCommentDto;
import com.salesianostriana.dam.dyscotkeov1.comment.dto.NewCommentDto;
import com.salesianostriana.dam.dyscotkeov1.comment.model.Comment;
import com.salesianostriana.dam.dyscotkeov1.comment.repository.CommentRespository;
import com.salesianostriana.dam.dyscotkeov1.exception.notfound.CommentNotFoundException;
import com.salesianostriana.dam.dyscotkeov1.exception.notfound.PostNotFoundException;
import com.salesianostriana.dam.dyscotkeov1.post.dto.ViewPostDto;
import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.post.repository.PostRepository;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import com.salesianostriana.dam.dyscotkeov1.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty())
            throw new PostNotFoundException(id);

        comment.addPost(post.get());
        comment.addUser(user);
        postRepository.save(post.get());
        commentRespository.save(comment);

        return ViewPostDto.of(post.get());
    }

    public Post edit(NewCommentDto newCommentDto, Long id) {

        Optional<Comment> comment = commentRespository.findById(id);

        if (comment.isPresent()){
            Optional<Post> post = postRepository.findById(comment.get().getCommentedPost().getId());
            if (post.isPresent() && post.get().getComments().contains(comment.get())){
                comment.map(old -> {
                    old.setContent(newCommentDto.getContent());
                    old.setImgPath(newCommentDto.getImgPath());
                    return commentRespository.save(old);
                });
                return post.get();
            }
            throw new PostNotFoundException(comment.get().getCommentedPost().getId());
        }
        throw new CommentNotFoundException(id);
    }

    public ResponseEntity<?> delete(Long id){
        Optional<Comment> comment = commentRespository.findById(id);

        if (comment.isPresent()) {
            Optional<Post> post = postRepository.findById(comment.get().getCommentedPost().getId());
            if (post.isPresent()) {
                if (post.get().getComments().contains(comment.get())){
                    comment.get().removePost(comment.get().getCommentedPost());
                    comment.get().removeUser();
                    commentRespository.delete(comment.get());
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            throw new PostNotFoundException(comment.get().getCommentedPost().getId());
        }
        throw new CommentNotFoundException(id);
    }
}
