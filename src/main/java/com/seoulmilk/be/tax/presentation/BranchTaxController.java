package com.seoulmilk.be.tax.presentation;

import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.tax.application.BranchTaxService;
import com.seoulmilk.be.tax.dto.response.BranchTaxFilterResponseList;
import com.seoulmilk.be.tax.presentation.api.BranchTaxApi;
import com.seoulmilk.be.taxvalidation.dto.request.BranchTaxFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.seoulmilk.be.global.dto.SuccessCode.BRANCH_TAX_FILTER_SUCCESS;

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
}
