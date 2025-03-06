package com.seoulmilk.be.tax.presentation;

import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.tax.application.BranchTaxService;
import com.seoulmilk.be.tax.dto.response.BranchTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.BranchTaxFilterResponseList;
import com.seoulmilk.be.tax.dto.response.OfficeTaxDetailResponse;
import com.seoulmilk.be.tax.presentation.api.BranchTaxApi;
import com.seoulmilk.be.taxvalidation.dto.request.BranchTaxFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seoulmilk.be.global.dto.SuccessCode.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tax-invoices/branch")
public class BranchTaxController implements BranchTaxApi {
    private final BranchTaxService branchTaxService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filter")
    @Override
    public SuccessResponse<BranchTaxFilterResponseList> findBranchTaxByFilters(
            @ModelAttribute BranchTaxFilterRequest request
    ) {
        return SuccessResponse.of(BRANCH_TAX_FILTER_SUCCESS,
                branchTaxService.findBranchTaxByFilters(request));
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
