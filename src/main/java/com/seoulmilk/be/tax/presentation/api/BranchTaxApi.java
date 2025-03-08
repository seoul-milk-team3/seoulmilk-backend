package com.seoulmilk.be.tax.presentation.api;

import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.tax.dto.response.BranchTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.BranchTaxFilterResponseList;
import com.seoulmilk.be.tax.dto.request.BranchTaxFilterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Branch", description = "대리점 조회 API")
public interface BranchTaxApi {

    @Operation(
            summary = "대리점 세금 계산서 필터링 조회",
            description = "로그인한 대리점의 세금 계산서를 필터 조건에 맞춰 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "지점 세금 계산서가 필터 조건에 맞게 성공적으로 조회되었습니다."
            )
    })
    SuccessResponse<BranchTaxFilterResponseList> findBranchTaxByFilters(
            @Parameter(description = "대리점 세금 계산서 필터 조건")
            @ModelAttribute BranchTaxFilterRequest request
    );

    @Operation(
            summary = "대리점 세금 계산서 상세 조회",
            description = "대리점 세금 계산서 상세 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "대리점 세금계산서 상세 조회가 성공적으로 완료되었습니다."
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인한 사용자와 세금계산서의 대리점이 다릅니다."
            )
    })
    SuccessResponse<BranchTaxDetailResponse> findBranchTaxDetail(
            @Parameter(description = "세금계산서 ID")
            @RequestParam Long taxId
    );
}
