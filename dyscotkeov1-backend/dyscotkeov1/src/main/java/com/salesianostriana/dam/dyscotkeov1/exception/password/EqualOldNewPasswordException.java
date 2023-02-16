package com.salesianostriana.dam.dyscotkeov1.exception.password;

import javax.persistence.EntityNotFoundException;

public class EqualOldNewPasswordException extends EntityNotFoundException {
    public EqualOldNewPasswordException() {
        super("Las contraseña introducida no es correcta, compruebe sus credenciales");
    }
}
