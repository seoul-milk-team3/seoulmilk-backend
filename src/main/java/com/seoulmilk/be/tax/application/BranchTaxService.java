package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.tax.dto.response.BranchTaxFilterResponse;
import com.seoulmilk.be.tax.dto.response.BranchTaxFilterResponseList;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.taxvalidation.dto.request.BranchTaxFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchTaxService {
    private final NtsTaxRepository ntsTaxRepository;

    public BranchTaxFilterResponseList findBranchTaxByFilters(BranchTaxFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        List<BranchTaxFilterResponse> filteredTax = ntsTaxRepository.findBranchTaxByFilters(request, pageable);
        return BranchTaxFilterResponseList.of(filteredTax, filteredTax.size());
    }
}
