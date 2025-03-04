package com.seoulmilk.be.tax.presentation.api;

import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequest;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
            summary = "세금 계산서 이미지와 분석 결과를 단일 저장하기",
            description = "세금 계산서 이미지와 분석 결과를 단일 저장합니다. \n" + "기획 과정에서 단일 기능 다중 기능 여부에 따라 선택 사용하시면 될 것 같아요"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "세금 계산서 이미지와 분석 결과가 성공적으로 저장되었습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "[\n" +
                                    "  {\n" +
                                    "    \"requestId\": \"b3da3627-9e14-4071-801a-1a8c8405b34e\",\n" +
                                    "    \"fields\": [\n" +
                                    "      { \"name\": \"공급자 등록번호\", \"inferText\": \"744-32-00722\" },\n" +
                                    "      { \"name\": \"작성일자\", \"inferText\": \"2024-06-30\" },\n" +
                                    "      { \"name\": \"공급가액\", \"inferText\": \"2,752,800\" },\n" +
                                    "      { \"name\": \"승인번호\", \"inferText\": \"20240630-10240701-92376019\" },\n" +
                                    "      { \"name\": \"공급받는자 등록번호\", \"inferText\": \"314-06-84750\" },\n" +
                                    "      { \"name\": \"공급받는자 상호\", \"inferText\": \"대전중앙유통\" },\n" +
                                    "      { \"name\": \"공급받는자 사업장 주소\", \"inferText\": \"대전광역시 서구변동254-180\" },\n" +
                                    "      { \"name\": \"합계금액\", \"inferText\": \"2.752.800\" }\n" +
                                    "    ]\n" +
                                    "  }\n" +
                                    "]")
                    )
            )
    })
    ResponseEntity<?> saveTaxInvoices(
            @RequestPart TaxInvoicesSaveRequest request,
            @RequestPart MultipartFile file
    );

    @Operation(
            summary = "세금 계산서 이미지와 분석 결과를 다중 저장하기",
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
