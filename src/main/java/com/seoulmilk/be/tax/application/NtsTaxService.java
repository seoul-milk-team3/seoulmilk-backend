package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.global.application.SimpleStorageService;
import com.seoulmilk.be.tax.application.ext.ClovaOcrClient;
import com.seoulmilk.be.tax.application.ext.ClovaOcrProperties;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.RegionType;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.request.ClovaOcrRequest;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import com.seoulmilk.be.tax.dto.response.BeforeValidateTaxResponse;
import com.seoulmilk.be.tax.dto.response.BeforeValidateTaxResponseList;
import com.seoulmilk.be.tax.dto.response.ClovaOcrResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
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
    private final SimpleStorageService simpleStorageService;
    private final ClovaOcrClient clovaOcrClient;
    private final ClovaOcrProperties clovaOcrProperties;

    public void analyzeTaxInvoices(List<MultipartFile> files) {

        List<ClovaOcrResponse> responses = files.stream()
                .map(file ->
                        clovaOcrClient.getOcrResult(
                                clovaOcrProperties.secrets(),
                                ClovaOcrRequest.fromMultipartFile(file, clovaOcrProperties)
                        ))
                .toList();

        saveTaxFromOcr(responses, files);
    }

    private void saveTaxFromOcr(List<ClovaOcrResponse> responses, List<MultipartFile> files) {

        List<String> imageUrlList = files.stream()
                .map(file -> simpleStorageService.uploadFile(file, "tax-invoices"))
                .toList();

        TaxInvoicesSaveRequestList responseList = TaxInvoicesSaveRequestList.of(responses, files);

        responseList.requests()
                .forEach(request ->
                        {
                            String imageUrl = imageUrlList.get(responseList.requests().indexOf(request));
                            NtsTax ntsTax = request.toNtsTax(request, imageUrl);

                            ntsTaxRepository.save(ntsTax);
                        }
                );
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
                pageable
        );

        return BeforeValidateTaxResponseList.of(results, results.size());
    }
}