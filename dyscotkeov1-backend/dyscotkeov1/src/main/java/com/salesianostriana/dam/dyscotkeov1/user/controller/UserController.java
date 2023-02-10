package com.salesianostriana.dam.dyscotkeov1.user.controller;

import com.salesianostriana.dam.dyscotkeov1.security.jwt.access.JwtProvider;
import com.salesianostriana.dam.dyscotkeov1.security.jwt.refresh.RefreshToken;
import com.salesianostriana.dam.dyscotkeov1.security.jwt.refresh.RefreshTokenService;
import com.salesianostriana.dam.dyscotkeov1.user.dto.GetUserDto;
import com.salesianostriana.dam.dyscotkeov1.user.dto.JwtUserResponse;
import com.salesianostriana.dam.dyscotkeov1.user.dto.LoginDto;
import com.salesianostriana.dam.dyscotkeov1.user.dto.NewUserDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authManager;

    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/")
    public GetPageDto<GetUserDto> findAll(
            @RequestParam(value = "s", defaultValue = "") String search,
            @PageableDefault(size = 20, page = 0) Pageable pageable){

        List<SearchCriteria> params = Extractor.extractSearchCriteriaList(search);
        return userService.findAll(params, pageable);
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

        refreshTokenService.deleteByUser(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JwtUserResponse.of(user, token, refreshToken.getToken()));


    }

    @PostMapping("/register")
    public ResponseEntity<GetUserDto> createUserWithUserRole(@RequestBody NewUserDto createUserRequest) {
        User user = userService.createUserWithUserRole(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(GetUserDto.of(user));
    }

}
