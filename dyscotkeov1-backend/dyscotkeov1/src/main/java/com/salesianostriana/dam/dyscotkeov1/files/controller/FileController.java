package com.salesianostriana.dam.dyscotkeov1.files.controller;

import com.salesianostriana.dam.dyscotkeov1.exception.file.NotAllowedCountFilesException;
import com.salesianostriana.dam.dyscotkeov1.files.dto.FileResponse;
import com.salesianostriana.dam.dyscotkeov1.files.service.FIleService;
import com.salesianostriana.dam.dyscotkeov1.files.service.StorageService;
import com.salesianostriana.dam.dyscotkeov1.files.utils.MediaTypeUrlResource;
import com.salesianostriana.dam.dyscotkeov1.post.service.PostService;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import com.salesianostriana.dam.dyscotkeov1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;
    private final UserService userService;
    private final FIleService fIleService;



    @PostMapping("/upload/post/{id}")
    public ResponseEntity<?> upload(@PathVariable Long id, @RequestPart("files") MultipartFile[] files, @AuthenticationPrincipal User user) {

        if (files.length > 4)
            throw new NotAllowedCountFilesException();

        List<FileResponse> result = Arrays.stream(files)
                .map(fIleService::uploadFile)
                .toList();

        fIleService.addToPost(id, user, result);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }


    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file, @AuthenticationPrincipal User user) {

        FileResponse response = fIleService.uploadFile(file);
        userService.setImg(response.getName(), user);

        return ResponseEntity.created(URI.create(response.getUri())).body(response);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename){
        MediaTypeUrlResource resource =
                (MediaTypeUrlResource) storageService.loadAsResource(filename);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", resource.getType())
                .body(resource);
    }

    @GetMapping("/profileImg")
    public ResponseEntity<Resource> getProfileImg(@AuthenticationPrincipal User user){

        MediaTypeUrlResource resource =
                (MediaTypeUrlResource) storageService.loadAsResource(user.getImgPath());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", resource.getType())
                .body(resource);
    }

    @GetMapping("userImg/{userName}")
    public ResponseEntity<Resource> getUserImg(@PathVariable String userName){
        User user = userService.findByUserName(userName);

        MediaTypeUrlResource resource =
                (MediaTypeUrlResource) storageService.loadAsResource(user.getImgPath());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", resource.getType())
                .body(resource);
    }

}
