package com.salesianostriana.dam.dyscotkeov1.search.specifications.user;

import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import com.salesianostriana.dam.dyscotkeov1.search.specifications.GSBuilder;
import com.salesianostriana.dam.dyscotkeov1.search.util.SearchCriteria;

import java.util.List;

public class USBuilder extends GSBuilder<User> {
    public USBuilder(List<SearchCriteria> params) {
        super(params, User.class);
    }
}
