package com.salesianostriana.dam.dyscotkeov1.exception.accesdenied;

import javax.persistence.EntityNotFoundException;

public class CommentDeniedAccessException extends EntityNotFoundException {
    public CommentDeniedAccessException(){
        super("No tienes permiso para modificar un comentario de otro usuario");
    }
}
