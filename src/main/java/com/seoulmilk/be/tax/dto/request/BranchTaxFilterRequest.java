package com.seoulmilk.be.tax.dto.request;

import com.seoulmilk.be.tax.domain.type.PayStatus;
import com.seoulmilk.be.tax.domain.type.ResultType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchTaxFilterRequest {
    private LocalDate startDate;
    private LocalDate endDate;

    @Schema(defaultValue = "NORMAL")
    private ResultType resultType;  //type String -> ResultType 으로 수정함
    private PayStatus payStatus;

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 8;
}
