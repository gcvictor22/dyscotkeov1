package com.salesianostriana.dam.dyscotkeov1.exception.notfound;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(Long id){
        super(String.format("The user with: %d does not exist", id));
    }

}
