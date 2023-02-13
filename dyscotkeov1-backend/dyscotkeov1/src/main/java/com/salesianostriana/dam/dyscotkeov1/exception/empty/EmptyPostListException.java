package com.salesianostriana.dam.dyscotkeov1.exception.empty;

import javax.persistence.EntityNotFoundException;

public class EmptyPostListException extends EntityNotFoundException {

    public EmptyPostListException(){
        super("No posts were found");
    }

}
