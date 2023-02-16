package com.salesianostriana.dam.dyscotkeov1.exception.badrequest;

import javax.persistence.EntityNotFoundException;

public class FileInPostBadRequestException extends EntityNotFoundException {
    public FileInPostBadRequestException(String s){
        super("No hay ninguna publicación con nombre "+s+" en el post");
    }
}
