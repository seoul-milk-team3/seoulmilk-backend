package com.seoulmilk.be.tax.presentation;

import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.tax.application.OfficeTaxService;
import com.seoulmilk.be.tax.domain.type.RegionType;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import com.seoulmilk.be.tax.dto.response.OfficeTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponseList;
import com.seoulmilk.be.tax.dto.response.OfficeValidateAbnormalTaxResponseList;
import com.seoulmilk.be.tax.presentation.api.OfficeTaxApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.seoulmilk.be.global.dto.SuccessCode.*;

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
            @RequestParam(required = false) RegionType region,
            @RequestParam(required = false) String searchSupplierName,
            @RequestParam(required = false) ResultType resultType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {
        OfficeTaxFilterResponseList response = officeTaxService.findOfficeTaxByFilters(startYearAndMonth, endYearAndMonth, region, searchSupplierName, resultType, page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(OFFICE_TAX_FILTER_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<?> findOfficeTaxDetail(
            @RequestParam Long taxId
    ) {
        OfficeTaxDetailResponse response = officeTaxService.findOfficeTaxDetail(taxId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(OFFICE_TAX_DETAIL_SUCCESS, response));
    }

    @GetMapping("/abnormal-list")
    public ResponseEntity<?> validateOfficeTax(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {
        OfficeValidateAbnormalTaxResponseList response = officeTaxService.validateAbnormalOfficeTax(page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(OFFICE_TAX_FILTER_SUCCESS, response));
    }

    @Override
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveTaxInvoicesList(
            @RequestPart TaxInvoicesSaveRequestList requestList,
            @RequestParam Long taxId
    ) {
        officeTaxService.saveTaxInvoicesList(requestList, taxId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(SAVE_TAX_SUCCESS));
    }
}