package com.seoulmilk.be.tax.application.office;

import com.seoulmilk.be.auth.application.AuthService;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.RegionType;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import com.seoulmilk.be.tax.dto.response.office.OfficeTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.office.OfficeTaxFilterResponse;
import com.seoulmilk.be.tax.dto.response.office.OfficeTaxFilterResponseList;
import com.seoulmilk.be.tax.dto.response.office.OfficeValidateAbnormalTaxResponseList;
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
    private final AuthService authService;

    public OfficeTaxFilterResponseList findOfficeTaxByFilters(LocalDate startYearAndMonth,
                                                              LocalDate endYearAndMonth,
                                                              RegionType region,
                                                              String searchSupplierName,
                                                              ResultType resultType,
                                                              int page,
                                                              int size) {

        String isValidated = "1";
        Pageable pageable = PageRequest.of(page - 1, size);
        List<OfficeTaxFilterResponse> result = ntsTaxRepository.findOfficeTaxByFilters(startYearAndMonth, endYearAndMonth, region, searchSupplierName, resultType, isValidated, pageable, authService.getLoginUser());

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
                pageable,
                authService.getLoginUser()
        );

        return OfficeValidateAbnormalTaxResponseList.of(results, results.size());
    }

    public OfficeTaxDetailResponse findOfficeTaxDetail(Long taxId) {
        NtsTax tax = ntsTaxRepository.findById(taxId)
                .orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND));

        return OfficeTaxDetailResponse.from(tax);
    }

    @Transactional
    public void saveTaxInvoicesList(TaxInvoicesSaveRequestList requestList, Long taxId) {
        NtsTax ntsTax = ntsTaxRepository.findById(taxId)
                .orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND));

        requestList.requests().forEach(request -> {
            NtsTax updated = request.toNtsTax(request, ntsTax.getImageUrl(), authService.getLoginUser());
            ntsTax.updateNtstax(updated);
        });

        ntsTaxRepository.save(ntsTax);
    }
}