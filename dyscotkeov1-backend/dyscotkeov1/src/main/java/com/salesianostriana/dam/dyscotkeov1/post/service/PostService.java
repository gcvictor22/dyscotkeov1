package com.salesianostriana.dam.dyscotkeov1.post.service;

import com.salesianostriana.dam.dyscotkeov1.error.GlobalRestControllerAdvice;
import com.salesianostriana.dam.dyscotkeov1.exception.accesdenied.PostAccessDeniedException;
import com.salesianostriana.dam.dyscotkeov1.exception.empty.EmptyPostListException;
import com.salesianostriana.dam.dyscotkeov1.exception.notfound.PostNotFoundException;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebRequestDataBinder;

import java.time.LocalDateTime;
import java.util.List;
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
                .imgPath(newPostDto.getImgPath())
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
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty())
            throw new PostNotFoundException(id);

        return post.get();
    }

    public Optional<Post> findByIdToDelete(Long id){
        return postRepository.findById(id);
    }

    public Post edit(Long id, NewPostDto newPostDto) {

        return postRepository.findById(id)
                .map(old -> {
                    old.setAffair(newPostDto.getAffair());
                    old.setContent(newPostDto.getContent());
                    old.setImgPath(newPostDto.getImgPath());
                    return postRepository.save(old);
                }).orElseThrow(() -> new  PostNotFoundException(id));
    }

    public void deleteById(Post post, User loggedUser) {
        if (loggedUser.getId() == post.getUserWhoPost().getId()){
            User user = post.getUserWhoPost();
            post.removeUser(user);
            userRepository.save(user);
            post.setUserWhoPost(null);
            postRepository.delete(post);
        }else {
            throw new PostAccessDeniedException("No puedes borrar una publicación de otro usuario");
        }
    }

    public GetPostDto likeAPost(Long id, User user) {

        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()){
            throw new PostNotFoundException(id);
        }

        post.get().like(user, postRepository.existsLikeByUser(post.get().getId(), user.getId()));

        postRepository.save(post.get());
        userRepository.save(user);

        GetPostDto dto = GetPostDto.of(post.get());
        if (postRepository.existsLikeByUser(post.get().getId(), user.getId())){
            dto.setUsersWhoLiked(dto.getUsersWhoLiked()+1);
        }else {
            dto.setUsersWhoLiked(dto.getUsersWhoLiked()-1);
        }
        return dto;
    }
}
