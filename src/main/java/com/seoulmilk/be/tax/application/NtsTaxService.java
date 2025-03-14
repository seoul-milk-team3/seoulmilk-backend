package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.auth.application.AuthService;
import com.seoulmilk.be.tax.application.office.NtsTaxFacadeService;
import com.seoulmilk.be.tax.domain.type.RegionType;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.response.BeforeValidateTaxResponseList;
import com.seoulmilk.be.tax.dto.response.office.OfficeTaxFilterResponse;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtsTaxService {

    private final NtsTaxRepository ntsTaxRepository;
    private final AuthService authService;
    private final NtsTaxFacadeService ntsTaxFacadeService;

    public void analyzeTaxInvoices(List<MultipartFile> files) {
        ntsTaxFacadeService.analyzeTaxInvoices(files);
    }

    @Transactional(readOnly = true)
    public BeforeValidateTaxResponseList findListBeforeValidateTax(int page,
                                                                   int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<OfficeTaxFilterResponse> results = ntsTaxRepository.findOfficeTaxByFilters(
                null,
                null,
                RegionType.ALL,
                null,
                ResultType.ALL,
                "0",
                pageable,
                authService.getLoginUser()
        );

        return BeforeValidateTaxResponseList.of(results, results.size());
    }
}
