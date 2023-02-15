package com.salesianostriana.dam.dyscotkeov1.files.service;

import com.salesianostriana.dam.dyscotkeov1.files.dto.FileResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@AllArgsConstructor
public class FIleService {

    private final StorageService storageService;

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

}
