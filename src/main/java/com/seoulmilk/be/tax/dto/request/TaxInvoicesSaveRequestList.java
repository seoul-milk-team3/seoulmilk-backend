package com.seoulmilk.be.tax.dto.request;

import com.seoulmilk.be.tax.dto.response.ClovaOcrResponse;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Builder
public record TaxInvoicesSaveRequestList(
        List<TaxInvoicesSaveRequest> requests
) {

    public static TaxInvoicesSaveRequestList of(List<ClovaOcrResponse> ocrResponses, List<MultipartFile> files) {
        List<TaxInvoicesSaveRequest> requests = ocrResponses.stream()
                .map(ocrResponse -> {
                    String imageUrl = files.get(ocrResponses.indexOf(ocrResponse)).getOriginalFilename();

                    return new TaxInvoicesSaveRequest(ocrResponse.requestId(), mapFields(ocrResponse.images()));
                })
                .toList();

        return new TaxInvoicesSaveRequestList(requests);
    }

    private static List<TaxInvoicesSaveRequest.Field> mapFields(ClovaOcrResponse.Images[] images) {
        return Arrays.stream(images)
                .flatMap(image -> Arrays.stream(image.fields()))
                .map(field -> new TaxInvoicesSaveRequest.Field(field.name(), field.inferText()))
                .toList();
    }

}
