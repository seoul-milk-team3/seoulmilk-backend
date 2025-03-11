package com.seoulmilk.be.tax.application;

import com.seoulmilk.be.auth.service.AuthService;
import com.seoulmilk.be.global.application.SimpleStorageService;
import com.seoulmilk.be.tax.application.ext.ClovaOcrClient;
import com.seoulmilk.be.tax.application.ext.ClovaOcrProperties;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.dto.request.ClovaOcrRequest;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import com.seoulmilk.be.tax.dto.response.ClovaOcrResponse;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtsTaxFacadeService {

    private final NtsTaxRepository ntsTaxRepository;
    private final AuthService authService;
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
                            NtsTax ntsTax = request.toNtsTax(request, imageUrl, authService.getLoginUser());

                            ntsTaxRepository.save(ntsTax);
                        }
                );
    }
}
