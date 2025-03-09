package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.global.application.SimpleStorageService;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.RegionType;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import com.seoulmilk.be.tax.dto.response.*;
import com.seoulmilk.be.tax.exception.NtsTaxNotFoundException;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;


import java.time.LocalDate;
import java.util.List;

import static com.seoulmilk.be.tax.exception.errorcode.NtsTaxErrorCode.NTS_TAX_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfficeTaxService {

    private final NtsTaxRepository ntsTaxRepository;
    private final SimpleStorageService simpleStorageService;

    public OfficeTaxFilterResponseList findOfficeTaxByFilters(LocalDate startYearAndMonth,
                                                              LocalDate endYearAndMonth,
                                                              RegionType region,
                                                              String searchSupplierName,
                                                              ResultType resultType,
                                                              int page,
                                                              int size) {
        String isValidated = "1";
        Pageable pageable = PageRequest.of(page - 1, size);
        List<OfficeTaxFilterResponse> result = ntsTaxRepository.findOfficeTaxByFilters(startYearAndMonth, endYearAndMonth, region, searchSupplierName, resultType, isValidated, pageable);

        return OfficeTaxFilterResponseList.of(result, result.size());
    }

    public OfficeValidateAbnormalTaxResponseList validateAbnormalOfficeTax(int page,
                                                                           int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<OfficeTaxFilterResponse> results = ntsTaxRepository.findOfficeTaxByFilters(
                null,
                null,
                RegionType.ALL,
                null,
                ResultType.ABNORMAL,
                "1",
                pageable
        );

        return OfficeValidateAbnormalTaxResponseList.of(results, results.size());
    }

    public OfficeTaxDetailResponse findOfficeTaxDetail(Long taxId) {
        NtsTax tax = ntsTaxRepository.findById(taxId)
                .orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND));

        return OfficeTaxDetailResponse.from(tax);
    }

    public void saveTaxInvoicesList(TaxInvoicesSaveRequestList requestList, List<MultipartFile> files) {

        List<String> imageUrlList = files.stream()
                .map(file -> simpleStorageService.uploadFile(file, "tax-invoices"))
                .toList();

        requestList.requests()
                .forEach(request ->
                        {
                            String imageUrl = imageUrlList.get(requestList.requests().indexOf(request));
                            NtsTax ntsTax = request.toNtsTax(request, imageUrl);

                            ntsTaxRepository.save(ntsTax);
                        }
                );
    }


}