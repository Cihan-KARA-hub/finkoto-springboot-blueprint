package com.finkoto.chargestation.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties(
        {
                "pageable",
                "number",
                "numberOfElements",
                "first",
                "last",
                "empty"
        }
)
public class PageableResponseDto<T> extends PageImpl<T> {

    public PageableResponseDto(final List<T> content, final long totalElements, final Pageable pageable) {
        super(content, pageable, totalElements);
    }

    public int getPage() {
        return getNumber();
    }

    @JsonProperty("sort")
    public List<String> getSortList() {
        return getSort().stream().map(order -> order.getProperty() + "," + order.getDirection().name()).toList();
    }
}