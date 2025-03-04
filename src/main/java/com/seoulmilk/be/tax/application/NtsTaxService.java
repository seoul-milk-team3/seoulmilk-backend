package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.global.application.SimpleStorageService;
import com.seoulmilk.be.tax.application.ext.ClovaOcrClient;
import com.seoulmilk.be.tax.application.ext.ClovaOcrProperties;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.dto.request.ClovaOcrRequest;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequest;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import com.seoulmilk.be.tax.dto.response.ClovaOcrResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;

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

        log.info("responses: {}", responses);
        return responses;
    }

    public void saveTaxInvoices(TaxInvoicesSaveRequest request, MultipartFile file) {
        String imageUrl = simpleStorageService.uploadFile(file, "tax-invoices");

        NtsTax ntsTax = request.toNtsTax(request, imageUrl);

        ntsTaxRepository.save(ntsTax);
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

    public List<OfficeTaxFilterResponse> findOfficeTaxByFilters(LocalDate startYearAndMonth,   //파일 업로드 일자 필터링
                                                                LocalDate endYearAndMonth,
                                                                String region,  //지역 필터링
                                                                String searchSupplierName, //공급자 검색 필터링
                                                                String resultType,
                                                                int page,
                                                                int size)
    {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ntsTaxRepository.findOfficeTaxByFilters(startYearAndMonth, endYearAndMonth, region, searchSupplierName, resultType, pageable);
    }
}