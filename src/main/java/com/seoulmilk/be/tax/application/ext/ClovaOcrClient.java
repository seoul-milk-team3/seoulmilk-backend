package com.seoulmilk.be.tax.application.ext;

import com.seoulmilk.be.tax.dto.request.ClovaOcrRequest;
import com.seoulmilk.be.tax.dto.response.ClovaOcrResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "clova-tax-ocr", url = "${clova.ocr.url}")
public interface ClovaOcrClient {
    @PostMapping(value = "/infer", consumes = MediaType.APPLICATION_JSON_VALUE)
    ClovaOcrResponse getOcrResult(
            @RequestHeader("X-OCR-SECRET") String X_OCR_SECRET,
            @RequestBody ClovaOcrRequest clovaOcrRequest
    );
}