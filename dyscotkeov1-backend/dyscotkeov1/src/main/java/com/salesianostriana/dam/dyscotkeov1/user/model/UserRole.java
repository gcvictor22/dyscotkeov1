package com.salesianostriana.dam.dyscotkeov1.user.model;

public enum UserRole {

    USER, VERIFIED;

    public static boolean contains(String text) {

        try {
            UserRole.valueOf(text);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }


    }

}
