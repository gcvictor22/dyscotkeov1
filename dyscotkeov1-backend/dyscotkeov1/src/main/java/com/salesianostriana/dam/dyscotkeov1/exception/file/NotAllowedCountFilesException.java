package com.salesianostriana.dam.dyscotkeov1.exception.file;

import javax.persistence.EntityNotFoundException;

public class NotAllowedCountFilesException extends EntityNotFoundException {
    public NotAllowedCountFilesException(){
        super("No se pueden subir más de 4 imágenes a una publicación");
    }
}
