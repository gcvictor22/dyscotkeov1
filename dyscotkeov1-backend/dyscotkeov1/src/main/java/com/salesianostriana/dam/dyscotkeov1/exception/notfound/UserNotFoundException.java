package com.salesianostriana.dam.dyscotkeov1.exception.notfound;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(UUID id){
        super(String.format("The user with: "+id+" does not exist"));
    }

    public UserNotFoundException(String username){
        super(String.format("The user: "+username+" does not exist"));
    }

}
