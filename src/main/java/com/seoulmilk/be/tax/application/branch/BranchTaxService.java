package com.seoulmilk.be.tax.application.branch;

import com.seoulmilk.be.auth.application.AuthService;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.PayStatus;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.response.branch.BranchTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.branch.BranchTaxFilterResponse;
import com.seoulmilk.be.tax.dto.response.branch.BranchTaxFilterResponseList;
import com.seoulmilk.be.tax.exception.NtsTaxNotFoundException;
import com.seoulmilk.be.tax.exception.UnauthorizedTaxUserException;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.auth.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.seoulmilk.be.tax.exception.errorcode.NtsTaxErrorCode.NTS_TAX_NOT_FOUND;
import static com.seoulmilk.be.tax.exception.errorcode.NtsTaxErrorCode.UNAUTHORIZED_TAX_USER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchTaxService {
    private final AuthService authService;
    private final NtsTaxRepository ntsTaxRepository;

    public BranchTaxFilterResponseList findBranchTaxByFilters(LocalDate startDate, LocalDate endDate, ResultType resultType, PayStatus payStatus, int page, int size) {
        User user = authService.getLoginUser();
        Pageable pageable = PageRequest.of(page - 1, size);
        List<BranchTaxFilterResponse> filteredTax = ntsTaxRepository.findBranchTaxByFiltersAndUser(startDate, endDate, resultType, payStatus, user, pageable);
        return BranchTaxFilterResponseList.of(filteredTax, filteredTax.size());
    }

    public BranchTaxDetailResponse findBranchTaxDetail(Long taxId) {
        User user = authService.getLoginUser();
        NtsTax ntsTax = ntsTaxRepository.findById(taxId)
                .orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND));
        if (!getRemovedBar(ntsTax.getSuId()).equals(getRemovedBar(user.getBusinessId()))) {
            throw new UnauthorizedTaxUserException(UNAUTHORIZED_TAX_USER);
        }

        return BranchTaxDetailResponse.from(ntsTax);
    }

    private String getRemovedBar(String barStr) {
        return barStr.replace("-", "");
    }
}
