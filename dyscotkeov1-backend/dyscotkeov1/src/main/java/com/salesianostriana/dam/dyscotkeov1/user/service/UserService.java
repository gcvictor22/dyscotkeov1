package com.salesianostriana.dam.dyscotkeov1.user.service;

import com.salesianostriana.dam.dyscotkeov1.post.repository.PostRepository;
import com.salesianostriana.dam.dyscotkeov1.security.jwt.access.JwtProvider;
import com.salesianostriana.dam.dyscotkeov1.security.jwt.refresh.RefreshTokenService;
import com.salesianostriana.dam.dyscotkeov1.user.dto.GetUserDto;
import com.salesianostriana.dam.dyscotkeov1.user.dto.NewUserDto;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import com.salesianostriana.dam.dyscotkeov1.user.model.UserRole;
import com.salesianostriana.dam.dyscotkeov1.user.repository.UserRepository;
import com.salesianostriana.dam.dyscotkeov1.exception.empty.EmptyUserListException;
import com.salesianostriana.dam.dyscotkeov1.page.dto.GetPageDto;
import com.salesianostriana.dam.dyscotkeov1.search.specifications.clients.CSBuilder;
import com.salesianostriana.dam.dyscotkeov1.search.util.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(NewUserDto createUser, EnumSet<UserRole> roles) {
        User user =  User.builder()
                .userName(createUser.getUsername())
                .password(passwordEncoder.encode(createUser.getPassword()))
                .imgPath(createUser.getAvatar())
                .fullName(createUser.getFullName())
                .roles(roles)
                .createdAt(createUser.getCreatedAt())
                .build();

        return userRepository.save(user);
    }

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
        return userRepository.findById(userId);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findDistinctByUserName(username);
    }

    public User createUserWithUserRole(NewUserDto createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(UserRole.USER));
    }

    @Transactional
    public List<User> userWithPostsWithComments(){
        List<User> res = userRepository.userPosts();

        if (!res.isEmpty())
            postRepository.postComments();

        return res;
    }
}
