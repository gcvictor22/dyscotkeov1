package com.salesianostriana.dam.dyscotkeov1.post.service;

import com.salesianostriana.dam.dyscotkeov1.exception.empty.EmptyUserListException;
import com.salesianostriana.dam.dyscotkeov1.page.dto.GetPageDto;
import com.salesianostriana.dam.dyscotkeov1.post.dto.GetPostDto;
import com.salesianostriana.dam.dyscotkeov1.post.dto.NewPostDto;
import com.salesianostriana.dam.dyscotkeov1.post.dto.ViewPostDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
            throw new EmptyUserListException();

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
            throw new EntityNotFoundException();

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
                }).orElseThrow(EntityNotFoundException::new);
    }

    public void deleteById(Post post) {
        User user = post.getUserWhoPost();
        post.removeUser(user);
        userRepository.save(user);
        post.setUserWhoPost(null);
        postRepository.delete(post);
    }

    public GetPostDto likeAPost(Long id, User user) {

        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()){
            throw new EntityNotFoundException();
        }

        List<Post> aux1 = user.getLikedPosts();
        List<User> aux2 = post.get().getUsersWhoLiked();
        if (!postRepository.existsLikeByUser(post.get().getId(), user.getId())){
            aux1.add(post.get());
            aux2.add(user);
        }else {
            aux1.remove(user.getLikedPosts().indexOf(post.get())+1);
            aux2.remove(post.get().getUsersWhoLiked().indexOf(user)+1);
        }

        postRepository.save(post.get());
        userRepository.save(user);

        return GetPostDto.of(post.get());

    }
}
