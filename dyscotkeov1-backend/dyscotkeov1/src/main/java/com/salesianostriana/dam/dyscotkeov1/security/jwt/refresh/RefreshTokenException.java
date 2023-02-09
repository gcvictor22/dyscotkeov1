package com.salesianostriana.dam.dyscotkeov1.security.jwt.refresh;

import com.salesianostriana.dam.dyscotkeov1.security.errorhandling.JwtTokenException;

public class RefreshTokenException extends JwtTokenException {

    public RefreshTokenException(String msg) {
        super(msg);
    }

}


