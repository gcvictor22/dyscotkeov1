package com.salesianostriana.dam.dyscotkeov1.files.service;

import com.salesianostriana.dam.dyscotkeov1.exception.accesdenied.PostAccessDeniedExeption;
import com.salesianostriana.dam.dyscotkeov1.exception.file.NotAllowedCountFilesException;
import com.salesianostriana.dam.dyscotkeov1.exception.notfound.PostNotFoundException;
import com.salesianostriana.dam.dyscotkeov1.files.dto.FileResponse;
import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.post.repository.PostRepository;
import com.salesianostriana.dam.dyscotkeov1.post.service.PostService;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FIleService {

    private final StorageService storageService;
    private final PostRepository postRepository;

    public FileResponse uploadFile(MultipartFile file) {
        String name = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        return FileResponse.builder()
                .name(name)
                .size(file.getSize())
                .type(file.getContentType())
                .uri(uri)
                .build();
    }

    public void addToPost(Long id, User loggedUser, List<FileResponse> result) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));

        if (!Objects.equals(loggedUser.getUsername(), post.getUserWhoPost().getUsername()))
            throw new PostAccessDeniedExeption();

        if (post.getImgPath().size() == 4)
            throw new NotAllowedCountFilesException();

        result.forEach(r -> {
            post.getImgPath().add(r.getName());
        });
        postRepository.save(post);
    }
}
