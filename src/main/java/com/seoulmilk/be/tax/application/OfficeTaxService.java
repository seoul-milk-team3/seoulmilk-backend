package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.dto.response.OfficeTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
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

    public List<OfficeTaxDetailResponse> findOfficeTaxDetail (Long taxId) {

        NtsTax tax = ntsTaxRepository.findById(taxId)
                .orElseThrow(() -> new IllegalArgumentException("해당 세금계산서가 존재하지 않습니다."));

        return List.of(OfficeTaxDetailResponse.of(tax));
    }
}