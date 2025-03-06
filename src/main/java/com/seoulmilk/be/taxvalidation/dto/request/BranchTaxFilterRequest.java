package com.seoulmilk.be.taxvalidation.dto.request;

import com.seoulmilk.be.tax.domain.type.PayStatus;
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
    private String resultType;
    private PayStatus payStatus;

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 8;
}
