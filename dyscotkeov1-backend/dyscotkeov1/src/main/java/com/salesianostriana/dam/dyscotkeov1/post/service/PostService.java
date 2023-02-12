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

        newPost.addUser(user);
        userRepository.save(user);

        return newPost;
    }

    public ViewPostDto findById(Long id) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty())
            throw new EntityNotFoundException();

        return ViewPostDto.of(post.get());
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
}
