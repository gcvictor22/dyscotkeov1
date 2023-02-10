package com.salesianostriana.dam.dyscotkeov1.user.model;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class EnumRoleConverter implements AttributeConverter<EnumSet<UserRole>, String> {

    private final String SEPARADOR = ", ";


    @Override
    public String convertToDatabaseColumn(EnumSet<UserRole> attribute) {

        if (!attribute.isEmpty()) {
            return attribute.stream()
                    .map(UserRole::name)
                    .collect(Collectors.joining(SEPARADOR));
        }
        return null;
    }

    @Override
    public EnumSet<UserRole> convertToEntityAttribute(String dbData) {
        if (dbData != null) {
            if (!dbData.isBlank()) { // isBlank Java 11
                return Arrays.stream(dbData.split(SEPARADOR)) // Separamos la cadena en un array de String y lo convertimos en Stream
                        .map(UserRole::valueOf) // Mapeamos cada cadena con su correspondiente valor de la enumeracion
                        .collect(Collectors.toCollection(() -> EnumSet.noneOf(UserRole.class))); // Los recogemos en la colección correspondiente
            }
        }
        return EnumSet.noneOf(UserRole.class);
    }

}
