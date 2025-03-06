package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.dto.response.OfficeTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
import com.seoulmilk.be.tax.exception.NtsTaxNotFoundException;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.seoulmilk.be.tax.exception.errorcode.NtsTaxErrorCode.NTS_TAX_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfficeTaxService {

    private final NtsTaxRepository ntsTaxRepository;

    public List<OfficeTaxFilterResponse> findOfficeTaxByFilters(LocalDate startYearAndMonth,
                                                                LocalDate endYearAndMonth,
                                                                String region,
                                                                String searchSupplierName,
                                                                String resultType,
                                                                int page,
                                                                int size)
    {
        String isValidated = "1";
        Pageable pageable = PageRequest.of(page - 1, size);
        return ntsTaxRepository.findOfficeTaxByFilters(startYearAndMonth, endYearAndMonth, region, searchSupplierName, resultType, isValidated, pageable);
    }

    public OfficeTaxDetailResponse findOfficeTaxDetail (Long taxId) {
        NtsTax tax = ntsTaxRepository.findById(taxId)
                .orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND));

        return OfficeTaxDetailResponse.of(tax);
    }
}