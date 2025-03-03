package com.seoulmilk.be.tax.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seoulmilk.be.tax.application.ext.ClovaOcrProperties;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClovaOcrRequest(
        String version,
        String requestId,
        long timestamp,
        String lang,
        List<Image> images
) {
    public record Image(
            String format,
            String name,
            String data, // Base64로 인코딩된 파일 데이터
            List<Integer> templateIds
    ) {
    }

    public static ClovaOcrRequest fromMultipartFile(MultipartFile file, ClovaOcrProperties properties) {
        Image image = makeImage(file, properties);

        return new ClovaOcrRequest(
                "V1",
                UUID.randomUUID().toString(),
                System.currentTimeMillis(),
                "ko",
                List.of(image)
        );
    }

    private static Image makeImage(MultipartFile file, ClovaOcrProperties properties) {
        String base64Data;

        try {
            base64Data = Base64.getEncoder().encodeToString(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Image(
                getFileExtension(file.getOriginalFilename()),
                file.getOriginalFilename(),
                base64Data,
                properties.templateIds()
        );
    }

    private static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "unknown";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
