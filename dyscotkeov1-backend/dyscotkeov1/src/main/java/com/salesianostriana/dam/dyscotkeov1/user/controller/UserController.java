package com.salesianostriana.dam.dyscotkeov1.user.controller;

import com.salesianostriana.dam.dyscotkeov1.security.jwt.JwtProvider;
import com.salesianostriana.dam.dyscotkeov1.user.dto.*;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import com.salesianostriana.dam.dyscotkeov1.user.service.UserService;
import com.salesianostriana.dam.dyscotkeov1.page.dto.GetPageDto;
import com.salesianostriana.dam.dyscotkeov1.search.util.Extractor;
import com.salesianostriana.dam.dyscotkeov1.search.util.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authManager;

    private final JwtProvider jwtProvider;


    @GetMapping("/")
    public GetPageDto<GetUserDto> findAll(
            @RequestParam(value = "s", defaultValue = "") String search,
            @PageableDefault(size = 20, page = 0) Pageable pageable){

        List<SearchCriteria> params = Extractor.extractSearchCriteriaList(search);
        return userService.findAll(params, pageable);
    }

    @GetMapping("/profile")
    public UserProfileDto viewProfile(@AuthenticationPrincipal User loggedUser){
        return UserProfileDto.of(loggedUser);
    }

    @GetMapping("/{id}")
    public UserProfileDto viewUser(@PathVariable UUID id){

        User user = userService.findById(id).get() ;

        return UserProfileDto.of(user);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtUserResponse> login(@RequestBody LoginDto loginRequest) {

        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JwtUserResponse.of(user, token));
    }

    @PostMapping("/register")
    public ResponseEntity<GetUserDto> createUserWithUserRole(@Valid @RequestBody NewUserDto newUserDto) {
        User user = userService.createUser(newUserDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(GetUserDto.of(user));
    }

    @PostMapping("/follow/{id}")
    public ResponseEntity<GetUserDto> follow(@AuthenticationPrincipal User loggedUser, @PathVariable UUID id){

        User user = userService.follow(loggedUser, id);

        return ResponseEntity.status(HttpStatus.CREATED).body(GetUserDto.of(user));
    }

    @PutMapping("/change/password")
    public GetUserDto changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,
                                                       @AuthenticationPrincipal User loggedUser) {

        User updateUser = userService.changePassword(loggedUser, changePasswordDto.getNewPassword());

        return GetUserDto.of(updateUser);
    }

    @PutMapping("/change/profile")
    public GetUserDto changeProfile(@Valid @RequestBody ChangeProfileDto changeProfileDto,
                                    @AuthenticationPrincipal User loggedUser){

        User updatedUser =  userService.changeProfile(changeProfileDto, loggedUser);

        return GetUserDto.of(updatedUser);
    }

}
