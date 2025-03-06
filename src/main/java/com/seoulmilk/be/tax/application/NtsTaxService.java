package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.global.application.SimpleStorageService;
import com.seoulmilk.be.tax.application.ext.ClovaOcrClient;
import com.seoulmilk.be.tax.application.ext.ClovaOcrProperties;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.dto.request.ClovaOcrRequest;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import com.seoulmilk.be.tax.dto.response.BeforeValidateTaxResponse;
import com.seoulmilk.be.tax.dto.response.ClovaOcrResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtsTaxService {

    private final NtsTaxRepository ntsTaxRepository;
    private final SimpleStorageService simpleStorageService;
    private final ClovaOcrClient clovaOcrClient;
    private final ClovaOcrProperties clovaOcrProperties;

    public List<ClovaOcrResponse> analyzeTaxInvoices(List<MultipartFile> files) {
        List<ClovaOcrResponse> responses = new ArrayList<>();

        files.forEach(file -> {
            ClovaOcrResponse ocrResult = clovaOcrClient.getOcrResult(
                    clovaOcrProperties.secrets(),
                    ClovaOcrRequest.fromMultipartFile(file, clovaOcrProperties)
            );

            responses.add(ocrResult);
        });

        return responses;
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

    public List<BeforeValidateTaxResponse> findListBeforeValidateTax(int page,
                                                                     int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<OfficeTaxFilterResponse> results = ntsTaxRepository.findOfficeTaxByFilters(
                null,
                null,
                null,
                null,
                null,
                "0",
                pageable
        );

        return results.stream()
                .map(BeforeValidateTaxResponse::from)
                .toList();
    }
}
