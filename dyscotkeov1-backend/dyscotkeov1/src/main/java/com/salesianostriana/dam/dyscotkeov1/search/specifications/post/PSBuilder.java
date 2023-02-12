package com.salesianostriana.dam.dyscotkeov1.search.specifications.post;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.search.specifications.GSBuilder;
import com.salesianostriana.dam.dyscotkeov1.search.util.SearchCriteria;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;

import java.util.List;

public class PSBuilder extends GSBuilder<Post> {
    public PSBuilder(List<SearchCriteria> params) {
        super(params, Post.class);
    }
}
