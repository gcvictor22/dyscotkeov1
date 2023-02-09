package com.salesianostriana.dam.dyscotkeov1.page.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class GetPageDto<T> {

    private List<T> content;
    private boolean last;
    private boolean first;
    private int totalPages;
    private Long totalElements;

    public GetPageDto(Page<T> page){
        this.content = page.getContent();
        this.last = page.isLast();
        this.first = page.isFirst();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

}
