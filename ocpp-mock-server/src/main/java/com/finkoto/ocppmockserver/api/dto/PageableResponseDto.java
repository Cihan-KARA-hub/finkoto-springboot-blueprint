package com.finkoto.ocppmockserver.api.dto;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public class PageableResponseDto<T> extends PagedModel<T> {
    public PageableResponseDto(final List<T> content, final long totalElements, final Pageable pageable) {
        super(new PageImpl<>(content, pageable, totalElements));
    }
}