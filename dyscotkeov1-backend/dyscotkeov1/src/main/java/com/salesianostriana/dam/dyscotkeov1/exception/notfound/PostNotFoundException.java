package com.salesianostriana.dam.dyscotkeov1.exception.notfound;

import javax.persistence.EntityNotFoundException;

public class PostNotFoundException extends EntityNotFoundException {

    public PostNotFoundException(Long id){
        super(String.format("The post with id: "+id+" does not exist"));
    }

}
