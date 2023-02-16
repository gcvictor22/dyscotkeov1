package com.salesianostriana.dam.dyscotkeov1.exception.file;

import javax.persistence.EntityNotFoundException;

public class NotAllowedCountFilesException extends EntityNotFoundException {
    public NotAllowedCountFilesException(){
        super("La cantidad de imágenes subida no es válida");
    }
}
