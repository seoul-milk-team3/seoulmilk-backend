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
            summary = "OCR로 분석된 세금 계산서 이미지와 분석 결과를 다중 저장하기",
            description = "세금 계산서 이미지와 분석 결과를 다중 저장합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "세금 계산서 이미지와 분석 결과가 성공적으로 저장되었습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"requests\": [\n" +
                                    "    {\n" +
                                    "      \"requestId\": \"1a2b3c4d-1111-2222-3333-444455556666\",\n" +
                                    "      \"fields\": [\n" +
                                    "        {\"name\": \"공급자 등록번호\", \"inferText\": \"305-07-11111\"},\n" +
                                    "        {\"name\": \"작성일자\", \"inferText\": \"2024-06-05\"},\n" +
                                    "        {\"name\": \"공급가액\", \"inferText\": \"11110000\"},\n" +
                                    "        {\"name\": \"승인번호\", \"inferText\": \"20202020-10101010-10101010\"},\n" +
                                    "        {\"name\": \"공급받는자 등록번호\", \"inferText\": \"101-01-10101\"},\n" +
                                    "        {\"name\": \"공급받는자 상호\", \"inferText\": \"더미상호\"},\n" +
                                    "        {\"name\": \"공급받는자 사업장주소\", \"inferText\": \"부산광역시 더미로 더미길1\"},\n" +
                                    "        {\"name\": \"합계금액\", \"inferText\": \"276000\"},\n" +
                                    "        {\"name\": \"공급자 주소\", \"inferText\": \"부산광역시 중구 더미11로 101\"},\n" +
                                    "        {\"name\": \"공급자명\", \"inferText\": \"부산우유협동조합고객센터\"}\n" +
                                    "      ]\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}")
                    )
            )
    })
    ResponseEntity<?> saveTaxInvoicesList(
            @RequestPart TaxInvoicesSaveRequestList requestList,
            @RequestPart List<MultipartFile> files
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
