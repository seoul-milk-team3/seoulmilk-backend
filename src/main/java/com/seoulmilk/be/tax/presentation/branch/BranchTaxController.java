package com.seoulmilk.be.tax.presentation.branch;

import com.seoulmilk.be.global.dto.response.SuccessResponse;
import com.seoulmilk.be.tax.application.branch.BranchTaxService;
import com.seoulmilk.be.tax.domain.type.PayStatus;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.response.branch.BranchTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.branch.BranchTaxFilterResponseList;
import com.seoulmilk.be.tax.presentation.api.BranchTaxApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.seoulmilk.be.global.domain.type.SuccessCode.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/tax-invoices/branch")
public class BranchTaxController implements BranchTaxApi {
    private final BranchTaxService branchTaxService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filter")
    @Override
    public SuccessResponse<BranchTaxFilterResponseList> findBranchTaxByFilters(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) ResultType resultType,
            @RequestParam(required = false) PayStatus payStatus,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {

        return SuccessResponse.of(BRANCH_TAX_FILTER_SUCCESS,
                branchTaxService.findBranchTaxByFilters(startDate, endDate, resultType, payStatus, page, size));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{taxId}")
    @Override
    public SuccessResponse<BranchTaxDetailResponse> findBranchTaxDetail(
            @PathVariable Long taxId
    ) {
        BranchTaxDetailResponse response = branchTaxService.findBranchTaxDetail(taxId);

        return SuccessResponse.of(BRANCH_TAX_DETAIL_SUCCESS, response);
    }
}
