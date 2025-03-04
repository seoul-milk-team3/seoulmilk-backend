package com.seoulmilk.be.tax.presentation;

import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.tax.application.NtsTaxService;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequest;
import com.seoulmilk.be.tax.dto.request.TaxInvoicesSaveRequestList;
import com.seoulmilk.be.tax.dto.response.ClovaOcrResponse;
import com.seoulmilk.be.tax.presentation.api.NtxTaxApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.seoulmilk.be.global.dto.SuccessCode.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tax-invoices")
public class NtsTaxController implements NtxTaxApi {

    private final NtsTaxService ntsTaxService;

    @Override
    @PostMapping(value = "/ocr", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> analyzeTaxInvoices(
            @RequestPart List<MultipartFile> files
    ) {
        List<ClovaOcrResponse> response = ntsTaxService.analyzeTaxInvoices(files);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(ANALYZE_TAX_SUCCESS, response));
    }

    @Override
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveTaxInvoices(
            @RequestPart TaxInvoicesSaveRequest request,
            @RequestPart MultipartFile file
    ) {
        ntsTaxService.saveTaxInvoices(request, file);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(SAVE_TAX_SUCCESS));
    }

    @Override
    @PostMapping(value = "/list", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveTaxInvoicesList(
            @RequestPart TaxInvoicesSaveRequestList requestList,
            @RequestPart List<MultipartFile> files
    ) {
        ntsTaxService.saveTaxInvoicesList(requestList, files);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(SAVE_TAX_SUCCESS));
    }
}
