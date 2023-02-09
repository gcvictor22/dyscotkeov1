package com.salesianostriana.dam.dyscotkeov1.user.controller;

import com.salesianostriana.dam.dyscotkeov1.user.dto.GetUserDto;
import com.salesianostriana.dam.dyscotkeov1.user.service.UserService;
import com.salesianostriana.dam.dyscotkeov1.page.dto.GetPageDto;
import com.salesianostriana.dam.dyscotkeov1.search.util.Extractor;
import com.salesianostriana.dam.dyscotkeov1.search.util.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public GetPageDto<GetUserDto> findAll(
            @RequestParam(value = "s", defaultValue = "") String search,
            @PageableDefault(size = 25, page = 0) Pageable pageable){

        List<SearchCriteria> params = Extractor.extractSearchCriteriaList(search);
        return userService.findAll(params, pageable);
    }

}
