package com.seoulmilk.be.taxvalidation.presentation.api;

import com.seoulmilk.be.global.dto.response.SuccessResponse;
import com.seoulmilk.be.taxvalidation.dto.request.InvoiceValidationRequest;
import com.seoulmilk.be.taxvalidation.dto.response.InvoiceVerificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Tax Validation", description = "세금계산서 검증 관련 API")
public interface TaxValidationApi {
    @Operation(
            summary = "간편 인증 전 세금계산서 검증",
            description = "간편 인증을 요청하여 세금계산서 검증을 홈택스에 요청합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "간편 인증을 요청이 성공적으로 진행되었습니다."
            )
    })
    SuccessResponse<String> validateInvoicePreVerified(
            @RequestBody List<InvoiceValidationRequest> request,

            @Parameter(in = ParameterIn.PATH, description = "간편 인증 방법 (1:카카오톡, 2:페이코, 3:삼성패스, 4:KB모바일, " +
                    "5:통신사(PASS), 6:네이버, 7:신한인증서, 8: toss, 9: 뱅크샐러드)", required = true)
            String loginTypeLevel
    ) throws InterruptedException;

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
    SuccessResponse<List<InvoiceVerificationResponse>> validateInvoicePostVerified(
            @RequestBody List<InvoiceValidationRequest> request
    );
}
