package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.auth.service.AuthService;
import com.seoulmilk.be.global.application.SimpleStorageService;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.RegionType;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import com.seoulmilk.be.tax.dto.response.OfficeTaxDetailResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponseList;
import com.seoulmilk.be.tax.dto.response.OfficeValidateAbnormalTaxResponseList;
import com.seoulmilk.be.tax.exception.NtsTaxNotFoundException;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.user.domain.User;
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
        User user = authService.getLoginUser();
//        user.getEmployeeId();   //여기서 로그인한 employeeid 와 세금계산서의 employeeId 가 같은지 확인
//        user.getBusinessId(); // 여기서 로그인한 businessId와 세금계산서의 suId 와 equal 인지 확인

        String isValidated = "1";
        Pageable pageable = PageRequest.of(page - 1, size);
        List<OfficeTaxFilterResponse> result = ntsTaxRepository.findOfficeTaxByFilters(startYearAndMonth, endYearAndMonth, region, searchSupplierName, resultType, isValidated, pageable, user);

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
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 세금 데이터가 없습니다: " + taxId)); //TODO: 예외처리 진행 예정

        requestList.requests().forEach(request -> {
            NtsTax updated = request.toNtsTax(request, ntsTax.getImageUrl());
            ntsTax.updateNtstax(updated);
        });
        ntsTaxRepository.save(ntsTax);
    }
}