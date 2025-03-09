package com.seoulmilk.be.tax.presentation.api;

import com.seoulmilk.be.tax.domain.type.RegionType;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Office", description = "본사 API")
public interface OfficeTaxApi {

    @Operation(
            summary = "세금 계산서 자료 조회하기",
            description = "본사의 세금 계산서 자료를 조건에 맞게 필터링 합니다." + '\n' +
                    """
                            - 예시)
                            - startYearAndMonth: "2024-01-01"
                            - endYearAndMonth: "2024-03-01"
                            - region: "대전"  *지역이름으로 조회해주세요.(전체/서울/대전/광주/울산/부산/경기/강원/충북/충남/전북/전남/경북/경남/제주)
                            - searchSupplierName: "대전더미유통" * (지역이름 + 더미유통) 으로 조합해서 조회해주세요.
                            - resultType: "NORMAL"  (정상조회:NORMAL, 비정상조회:ABNORMAL, 전제조회:ALL)
                            - page: 1
                            - size: 8
                            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "세금 계산서가 성공적으로 조회되었습니다."
            )
    })
    ResponseEntity<?> findOfficeTaxByFilters(
            @RequestParam(required = false) LocalDate startYearAndMonth,
            @RequestParam(required = false) LocalDate endYearAndMonth,
            @RequestParam(required = false) RegionType region,
            @RequestParam(required = false) String searchSupplierName,
            @RequestParam(required = false) ResultType resultType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size
    );

    @Operation(
            summary = "세금 계산서 상세 조회하기",
            description = "본사의 세금 계산서 상세를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "세금 계산서 상세가 성공적으로 조회되었습니다."
            )
    })
    ResponseEntity<?> findOfficeTaxDetail(
            @RequestParam Long taxId
    );

    @Operation(
            summary = "비정상 세금 계산서 리스트 조회",
            description = "본사의 비정상 세금 계산서를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "세금 계산서 상세가 성공적으로 조회되었습니다."
            )
    })
    ResponseEntity<?> validateOfficeTax(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size);


    @Operation(
            summary = "수정된 세금 계산서 저장",
            description = "수정된 비정상 세금 계산서를 DB에 저장합니다."
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
}
