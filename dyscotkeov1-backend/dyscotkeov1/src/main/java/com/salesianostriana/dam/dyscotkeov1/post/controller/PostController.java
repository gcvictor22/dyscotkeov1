package com.salesianostriana.dam.dyscotkeov1.post.controller;

import com.salesianostriana.dam.dyscotkeov1.page.dto.GetPageDto;
import com.salesianostriana.dam.dyscotkeov1.post.dto.GetPostDto;
import com.salesianostriana.dam.dyscotkeov1.post.dto.NewPostDto;
import com.salesianostriana.dam.dyscotkeov1.post.dto.ViewPostDto;
import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.post.service.PostService;
import com.salesianostriana.dam.dyscotkeov1.search.util.Extractor;
import com.salesianostriana.dam.dyscotkeov1.search.util.SearchCriteria;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public GetPageDto<GetPostDto> findAll(
            @RequestParam(value = "s", defaultValue = "") String search,
            @PageableDefault(size = 20, page = 0) Pageable pageable){

        List<SearchCriteria> params = Extractor.extractSearchCriteriaList(search);
        return postService.findAll(params, pageable);
    }

    @GetMapping("/{id}")
    public ViewPostDto viewPost(@PathVariable Long id){
        return postService.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<GetPostDto> create(@AuthenticationPrincipal User user,@Valid @RequestBody NewPostDto newPostDto){
        Post created = postService.save(newPostDto, user);

        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(createdURI).body(GetPostDto.of(created));
    }

    @PutMapping("/{id}")
    public ViewPostDto editPost(@PathVariable Long id, @RequestBody NewPostDto newPostDto){
        Post post = postService.edit(id, newPostDto);

        return ViewPostDto.of(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @AuthenticationPrincipal User user){
        Optional<Post> post = postService.findByIdToDelete(id);

        if (post.isPresent()){
            postService.deleteById(post.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        throw new EntityNotFoundException();
    }

}
