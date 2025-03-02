package com.seoulmilk.be.tax.presentation;

import com.seoulmilk.be.tax.application.NtsTaxOcrService;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequest;
import com.seoulmilk.be.tax.dto.response.ClovaOcrResponse;
import com.seoulmilk.be.tax.presentation.api.NtxTaxOcrApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class NtsTaxOcrController implements NtxTaxOcrApi {

    private final NtsTaxOcrService ntsTaxOcrService;

    @Override
    @PostMapping(value = "/analyze", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> analyzeTaxInvoices(
            @RequestPart MultipartFile file
    ) {
        ClovaOcrResponse response = ntsTaxOcrService.analyzeTaxInvoices(file);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    @PostMapping(value = "/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveTaxInvoices(
            @RequestPart TaxInvoicesSaveRequest request,
            @RequestPart MultipartFile file
    ) {
        ntsTaxOcrService.saveTaxInvoices(request, file);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("saveTaxInvoices");
    }
}
