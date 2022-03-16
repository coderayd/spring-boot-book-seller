package com.work.bookseller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class PurchaseRequestDto {

    @NotNull(message = "'userId' can not be null")
    @Range(min = 1, max = Integer.MAX_VALUE)
    private Integer userId;

    @NotNull(message = "'userId' can not be null")
    @Range(min = 1, max = Integer.MAX_VALUE)
    private Integer bookId;

    @NotNull(message = "'price' can not be null")
    @Min(value = 1, message = "'price' can be at least 1")
    private Double price;
}