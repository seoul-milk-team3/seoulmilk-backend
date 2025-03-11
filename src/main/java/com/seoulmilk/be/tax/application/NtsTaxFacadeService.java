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

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        Instant start = Instant.now();

        List<ClovaOcrResponse> responseList = files.stream()
                .map(file -> (clovaOcrClient.getOcrResult(clovaOcrProperties.secrets(),
                        ClovaOcrRequest.fromMultipartFile(file, clovaOcrProperties)))
                )
                .toList();

        Instant end = Instant.now();
        log.info("Sequential processing time: {} ms", Duration.between(start, end).toMillis());

        saveTaxFromOcr(responseList, files);
    }

    private void saveTaxFromOcr(List<ClovaOcrResponse> responses, List<MultipartFile> files) {

        List<String> imageUrlList = files.stream()
                .map(file ->
                        simpleStorageService.uploadFile(file, "tax-invoices"))
                .toList();

        TaxInvoicesSaveRequestList responseList = TaxInvoicesSaveRequestList.of(responses, files);

        List<NtsTax> ntsTaxes = responseList.requests().stream()
                .map( request -> {
                    int index = responseList.requests().indexOf(request);
                    return request.toNtsTax(request, imageUrlList.get(index), authService.getLoginUser());
                        })
                .toList();

        ntsTaxRepository.saveAll(ntsTaxes);
    }
}
