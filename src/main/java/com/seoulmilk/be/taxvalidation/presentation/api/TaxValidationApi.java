package com.seoulmilk.be.taxvalidation.presentation.api;

import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.taxvalidation.dto.response.InvoiceVerificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Tax Validation", description = "세금계산서 검증 관련 API")
public interface TaxValidationApi {
    @Operation(
            summary = "세금계산서 검증",
            description = "세금계산서 ID를 입력받아 검증을 요청합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "세금계산서 검증이 요청이 성공적으로 진행되었습니다."
            )
    })
    SuccessResponse<String> validateInvoicePreVerified(
            @Parameter(in = ParameterIn.PATH, description = "세금계산서 ID", required = true)
            Long taxId
    );

    @Operation(
            summary = "간편 인증 완료 후 세금계산서 검증",
            description = "간편 인증을 완료한 이후, 세금계산서 ID를 입력받아 검증합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "세금계산서 검증이 성공적으로 진행되었습니다."
            )
    })
    SuccessResponse<InvoiceVerificationResponse> validateInvoicePostVerified(
            @Parameter(in = ParameterIn.PATH, description = "세금계산서 ID", required = true)
            Long taxId
    );
}
