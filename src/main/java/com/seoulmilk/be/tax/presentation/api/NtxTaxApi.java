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
                                    "      \"requestId\": \"8c3141bd-2faa-4b48-87e7-32701280784b\",\n" +
                                    "      \"fields\": [\n" +
                                    "        {\"name\": \"공급자 등록번호\", \"inferText\": \"305-07-77873\"},\n" +
                                    "        {\"name\": \"작성일자\", \"inferText\": \"2024-06-30\"},\n" +
                                    "        {\"name\": \"공급가액\", \"inferText\": \"250909\"},\n" +
                                    "        {\"name\": \"승인번호\", \"inferText\": \"20240630-10240701-93672170\"},\n" +
                                    "        {\"name\": \"공급받는자 등록번호\", \"inferText\": \"305-81-48738\"},\n" +
                                    "        {\"name\": \"공급받는자 상호\", \"inferText\": \"로쏘(주)\"},\n" +
                                    "        {\"name\": \"공급받는자 사업장주소\", \"inferText\": \"대전 중구 은행동 145번지\"},\n" +
                                    "        {\"name\": \"합계금액\", \"inferText\": \"276000\"},\n" +
                                    "        {\"name\": \"공급자 주소\", \"inferText\": \"대전광역시 중구 대둔산로420번길 9(신성동)\"},\n" +
                                    "        {\"name\": \"공급자명\", \"inferText\": \"서울우유협동조합 보문고객센타\"}\n" +
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
