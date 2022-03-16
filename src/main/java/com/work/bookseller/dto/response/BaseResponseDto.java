package com.work.bookseller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BaseResponseDto<T> {

    private Integer code;
    private String description;
    private T data;
}
