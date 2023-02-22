package com.salesianostriana.dam.dyscotkeov1.post.service;

import com.salesianostriana.dam.dyscotkeov1.error.GlobalRestControllerAdvice;
import com.salesianostriana.dam.dyscotkeov1.error.model.ApiError;
import com.salesianostriana.dam.dyscotkeov1.exception.accesdenied.PostAccessDeniedExeption;
import com.salesianostriana.dam.dyscotkeov1.exception.badrequest.PostBadRequestToDeleteException;
import com.salesianostriana.dam.dyscotkeov1.exception.empty.EmptyPostListException;
import com.salesianostriana.dam.dyscotkeov1.exception.notfound.PostNotFoundException;
import com.salesianostriana.dam.dyscotkeov1.exception.token.JwtAccessDeniedHandler;
import com.salesianostriana.dam.dyscotkeov1.page.dto.GetPageDto;
import com.salesianostriana.dam.dyscotkeov1.post.dto.GetPostDto;
import com.salesianostriana.dam.dyscotkeov1.post.dto.NewPostDto;
import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.post.repository.PostRepository;
import com.salesianostriana.dam.dyscotkeov1.search.specifications.post.PSBuilder;
import com.salesianostriana.dam.dyscotkeov1.search.util.SearchCriteria;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import com.salesianostriana.dam.dyscotkeov1.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public GetPageDto<GetPostDto> findAll(List<SearchCriteria> params, Pageable pageable) {
        if (postRepository.findAll().isEmpty())
            throw new EmptyPostListException();

        PSBuilder psBuilder = new PSBuilder(params);

        Specification<Post> spec = psBuilder.build();
        Page<GetPostDto> pageGetClientDto = postRepository.findAll(spec, pageable).map(GetPostDto::of);

        return new GetPageDto<>(pageGetClientDto);
    }

    public Post save(NewPostDto newPostDto, User user){
        Post newPost = Post.builder()
                .affair(newPostDto.getAffair())
                .content(newPostDto.getContent())
                .postDate(LocalDateTime.now())
                .build();

        return postRepository.save(newPost);
    }

    public GetPostDto responseCreate(Post newPost, User user){
        newPost.addUser(user);
        userRepository.save(user);
        return GetPostDto.of(newPost);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post edit(Long id, NewPostDto newPostDto, User loggedUser) {

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));

        if (!Objects.equals(loggedUser.getUsername(), post.getUserWhoPost().getUsername())){
            throw new PostAccessDeniedExeption();
        }
        return postRepository.findById(id)
                .map(old -> {
                    old.setAffair(newPostDto.getAffair());
                    old.setContent(newPostDto.getContent());
                    return postRepository.save(old);
                }).orElseThrow(() -> new PostNotFoundException(id));
    }

    public ResponseEntity<?> deleteById(Long id, User loggedUser) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostBadRequestToDeleteException(id));

        if (!Objects.equals(loggedUser.getUsername(), post.getUserWhoPost().getUsername())){
            throw new PostAccessDeniedExeption();
        }

        postRepository.delete(post);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public GetPostDto likeAPost(Long id, User loggedUser) {

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        boolean b = postRepository.existsLikeByUser(post.getId(), loggedUser.getId());

        post.like(loggedUser, b);

        postRepository.save(post);
        userRepository.save(loggedUser);

        GetPostDto dto = GetPostDto.of(post);
        if (Objects.equals(dto.getUserWhoPost().getUserName(), loggedUser.getUsername())){
            if (b){
                dto.setUsersWhoLiked(dto.getUsersWhoLiked()-1);
            }else {
                dto.setUsersWhoLiked(dto.getUsersWhoLiked()+1);
            }
        }

        return dto;
    }
}
