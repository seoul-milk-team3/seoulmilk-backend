package com.seoulmilk.be.tax.presentation.api;

import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NtxTaxOcrApi {
    @Operation(
            summary = "clova ocr",
            description = "OCR 을 통해 이미지를 텍스트로 변환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OCR 이 성공적으로 완료되었습니다."
            )
    })
    ResponseEntity<?> analyzeTaxInvoices(
            @RequestPart List<MultipartFile> file
    );


    @Operation(
            summary = "세금 계산서 이미지와 분석 결과를 저장",
            description = "세금 계산서 이미지와 분석 결과를 저장합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "세금 계산서 이미지와 분석 결과가 성공적으로 저장되었습니다."
            )
    })
    ResponseEntity<?> saveTaxInvoices(
            @RequestPart TaxInvoicesSaveRequest request,
            @RequestPart MultipartFile file
    );
}
