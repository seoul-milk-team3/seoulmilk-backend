package com.seoulmilk.be.tax.presentation.api;

import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Tax", description = "Tax OCR API")
public interface NtxTaxApi {
    @Operation(
            summary = "세금 계산서 분석하기",
            description = "OCR 을 통해 세금계산서를 텍스트로 변환합니다."
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
            summary = "OCR 로 분석된 세금 계산서 이미지와 분석 결과를 다중 저장하기",
            description = "세금 계산서 이미지와 분석 결과를 다중 저장합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "세금 계산서 이미지와 분석 결과가 성공적으로 저장되었습니다."
            )
    })
    ResponseEntity<?> saveTaxInvoicesList(
            @RequestPart TaxInvoicesSaveRequestList requestList,
            @RequestPart List<MultipartFile> files
    );
}
