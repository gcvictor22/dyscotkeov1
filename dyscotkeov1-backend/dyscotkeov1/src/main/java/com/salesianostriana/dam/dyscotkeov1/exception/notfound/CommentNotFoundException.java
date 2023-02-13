package com.salesianostriana.dam.dyscotkeov1.exception.notfound;

import javax.persistence.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {

    public CommentNotFoundException(Long id){
        super(String.format("The post with: "+id+" does not exist"));
    }

}
