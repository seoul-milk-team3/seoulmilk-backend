package com.seoulmilk.be.tax.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "Office", description = "본사 API")
public interface OfficeTaxApi {

    @Operation(
            summary = "세금 계산서 자료 조회하기",
            description = "본사의 세금 계산서 자료를 조건에 맞게 필터링 합니다." + '\n' +
                    """
                    - 예시)
                    - startYearAndMonth: "2024-01-01"
                    - endYearAndMonth: "2024-03-01"
                    - region: "대전"  *지역이름으로 조회해주세요.(서울/대전/광주/울산/부산/경기)
                    - searchSupplierName: "대전더미유통" * (지역이름 + 더미유통) 으로 조합해서 조회해주세요.
                    - resultType: "NORMAL"  (정상조회:NORMAL, 비정상조회:ABNORMAL, 전제조회:ALL) *입력시 대소문자 상관없습니다.
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
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String searchSupplierName,
            @RequestParam(required = false) String resultType,
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
}
