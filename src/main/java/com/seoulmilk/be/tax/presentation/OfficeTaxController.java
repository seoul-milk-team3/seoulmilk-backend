package com.seoulmilk.be.tax.presentation;

import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.tax.application.OfficeTaxService;
import com.seoulmilk.be.tax.dto.response.OfficeTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
import com.seoulmilk.be.tax.presentation.api.OfficeTaxApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.seoulmilk.be.global.dto.SuccessCode.OFFICE_TAX_DETAIL_SUCCESS;
import static com.seoulmilk.be.global.dto.SuccessCode.OFFICE_TAX_FILTER_SUCCESS;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tax-invoices/office")
public class OfficeTaxController implements OfficeTaxApi {

    private final OfficeTaxService officeTaxService;

    @Override
    @GetMapping("/filter")
    public ResponseEntity<?> findOfficeTaxByFilters(
            @RequestParam(required = false) LocalDate startYearAndMonth,
            @RequestParam(required = false) LocalDate endYearAndMonth,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String searchSupplierName,
            @RequestParam(required = false) String resultType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size)
    {
        List<OfficeTaxFilterResponse> response = officeTaxService.findOfficeTaxByFilters(startYearAndMonth, endYearAndMonth, region, searchSupplierName, resultType, page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(OFFICE_TAX_FILTER_SUCCESS,response));
    }

    @GetMapping
    public ResponseEntity<?> findOfficeTaxDetail(
            @RequestParam Long taxId
    ) {
        OfficeTaxDetailResponse response = officeTaxService.findOfficeTaxDetail(taxId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(OFFICE_TAX_DETAIL_SUCCESS,response));
    }
}