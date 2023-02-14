package com.salesianostriana.dam.dyscotkeov1.exception.badrequest;

import javax.persistence.EntityNotFoundException;

public class PostBadRequestToDeleteException extends EntityNotFoundException {
    public PostBadRequestToDeleteException(Long id){
        super("No se ha encotrado un Post con el id: "+id);
    }
}
