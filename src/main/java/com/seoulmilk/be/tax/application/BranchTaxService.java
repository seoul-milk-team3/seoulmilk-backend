package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.auth.service.AuthService;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.dto.response.BranchTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.BranchTaxFilterResponse;
import com.seoulmilk.be.tax.dto.response.BranchTaxFilterResponseList;
import com.seoulmilk.be.tax.exception.NtsTaxNotFoundException;
import com.seoulmilk.be.tax.exception.UnauthorizedTaxUserException;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.taxvalidation.dto.request.BranchTaxFilterRequest;
import com.seoulmilk.be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.seoulmilk.be.tax.exception.errorcode.NtsTaxErrorCode.NTS_TAX_NOT_FOUND;
import static com.seoulmilk.be.tax.exception.errorcode.NtsTaxErrorCode.UNAUTHORIZED_TAX_USER;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchTaxService {
    private final AuthService authService;
    private final NtsTaxRepository ntsTaxRepository;

    public BranchTaxFilterResponseList findBranchTaxByFilters(BranchTaxFilterRequest request) {
        User user = authService.getLoginUser();
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        List<BranchTaxFilterResponse> filteredTax = ntsTaxRepository.findBranchTaxByFiltersAndUser(request, user, pageable);
        return BranchTaxFilterResponseList.of(filteredTax, filteredTax.size());
    }

    public BranchTaxDetailResponse findOfficeTaxDetail(Long taxId) {
        User user = authService.getLoginUser();
        NtsTax ntsTax = ntsTaxRepository.findById(taxId)
                .orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND));
        if (!ntsTax.getSuId().equals(user.getEmployeeId())) {
            throw new UnauthorizedTaxUserException(UNAUTHORIZED_TAX_USER);
        }

        return BranchTaxDetailResponse.of(ntsTax);
    }
}
