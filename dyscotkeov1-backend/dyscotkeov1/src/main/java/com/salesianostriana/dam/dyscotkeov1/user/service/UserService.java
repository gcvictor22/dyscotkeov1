package com.salesianostriana.dam.dyscotkeov1.user.service;

import com.salesianostriana.dam.dyscotkeov1.user.dto.GetUserDto;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import com.salesianostriana.dam.dyscotkeov1.user.repository.UserRepository;
import com.salesianostriana.dam.dyscotkeov1.exception.empty.EmptyUserListException;
import com.salesianostriana.dam.dyscotkeov1.page.dto.GetPageDto;
import com.salesianostriana.dam.dyscotkeov1.search.specifications.clients.CSBuilder;
import com.salesianostriana.dam.dyscotkeov1.search.util.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /*
        public PageDto<ProductDto> search(List<SearchCriteria> params, Pageable pageable){
        PSBuilder psBuilder = new PSBuilder(params);

        Specification<Product> spec = psBuilder.build();
        Page<ProductDto> pageProductDto = productRepository.findAll(spec, pageable).map(ProductDto::of);

        return new PageDto<>(pageProductDto);
    }
     */

    public GetPageDto<GetUserDto> findAll(List<SearchCriteria> params, Pageable pageable){
        if (userRepository.findAll().isEmpty())
            throw new EmptyUserListException();

        CSBuilder csBuilder = new CSBuilder(params);

        Specification<User> spec = csBuilder.build();
        Page<GetUserDto> pageGetClientDto = userRepository.findAll(spec, pageable).map(GetUserDto::of);

        return new GetPageDto<>(pageGetClientDto);
    }

    public boolean existsByUserName(String s) {
        return userRepository.existsByUserName(s);
    }

    public Optional<User> findById(UUID userId) {
        return findById(userId);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findDistinctByUserName(username);
    }
}
