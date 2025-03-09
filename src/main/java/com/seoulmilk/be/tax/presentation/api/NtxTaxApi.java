package com.seoulmilk.be.tax.presentation.api;

import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
            summary = "세금 계산서 분석 및 저장",
            description = "OCR 을 통해 세금계산서를 텍스트로 변환하여 DB에 저장합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "OCR 이 성공적으로 완료되어 세금계산서가 DB에 저장되었습니다."
            )
    })
    ResponseEntity<?> analyzeTaxInvoices(
            @RequestPart List<MultipartFile> file
    );



    @Operation(
            summary = "진위 여부 확인 전 리스트 조회",
            description = "OCR 을 통해 분석되고, 진위 여부 확인 전의 세금 계산서 리스트를 확인합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "진위 여부 확인 전 리스트가 성공적으로 조회되었습니다."
            )
    })
    ResponseEntity<?> findListBeforeValidateTax(
            @RequestPart int page,
            @RequestPart int size
    );
}
