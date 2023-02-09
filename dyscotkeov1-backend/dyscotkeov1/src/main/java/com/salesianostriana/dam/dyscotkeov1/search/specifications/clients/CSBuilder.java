package com.salesianostriana.dam.dyscotkeov1.search.specifications.clients;

import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import com.salesianostriana.dam.dyscotkeov1.search.specifications.GSBuilder;
import com.salesianostriana.dam.dyscotkeov1.search.util.SearchCriteria;

import java.util.List;

public class CSBuilder extends GSBuilder<User> {
    public CSBuilder(List<SearchCriteria> params) {
        super(params, User.class);
    }
}
