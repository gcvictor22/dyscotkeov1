package com.salesianostriana.dam.dyscotkeov1.user.service;

import com.salesianostriana.dam.dyscotkeov1.exception.notfound.UserNotFoundException;
import com.salesianostriana.dam.dyscotkeov1.user.dto.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(NewUserDto createUser, EnumSet<UserRole> roles) {
        User user =  User.builder()
                .userName(createUser.getUsername())
                .password(passwordEncoder.encode(createUser.getPassword()))
                .email(createUser.getEmail())
                .phoneNumber(createUser.getPhoneNumber())
                .imgPath(createUser.getImgPath())
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

    public boolean existsByEmail(String e) {
        return userRepository.existsByUserName(e);
    }

    public boolean existsByPhoneNumber(String p) {
        return userRepository.existsByUserName(p);
    }

    public Optional<User> findById(UUID userId) {

        User user = userRepository.findById(userId).get();

        if (userRepository.findById(userId).isEmpty())
            throw  new UserNotFoundException(userId);

        return Optional.of(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findDistinctByUserName(username);
    }

    public User createUser(NewUserDto createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(UserRole.USER));
    }

    public User changePassword(User loggedUser, String changePasswordDto) {

        return userRepository.findById(loggedUser.getId())
                .map(old -> {
                    old.setPassword(changePasswordDto);
                    return userRepository.save(old);
                })
                .orElseThrow(() -> new UserNotFoundException(loggedUser.getId()));

    }

    public User changeProfile(ChangeProfileDto changeProfileDto, User loggedUser) {
        return userRepository.findById(loggedUser.getId())
                .map(old -> {
                    old.setFullName(changeProfileDto.getFullName());
                    old.setUserName(changeProfileDto.getUsername());
                    old.setEmail(changeProfileDto.getEmail());
                    old.setPhoneNumber(changeProfileDto.getPhoneNumber());
                    old.setImgPath(changeProfileDto.getImgPath());
                    return userRepository.save(old);
                })
                .orElseThrow(() -> new UserNotFoundException(loggedUser.getId()));
    }

    public User follow(User loggedUser, UUID userToFollowId) {
        Optional<User> user = userRepository.findById(userToFollowId);

        if (user.isEmpty())
            throw new UserNotFoundException(userToFollowId);

        loggedUser.giveAFollow(user.get());
        userRepository.save(loggedUser);
        userRepository.save(user.get());

        return user.get();
    }
}
