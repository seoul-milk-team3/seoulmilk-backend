package com.seoulmilk.be.global.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class SimpleStorageServiceTest {

    @Autowired
    private SimpleStorageService simpleStorageService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 업로드와 삭제를 한번에 확인하기 위해 테스트를 하나로 합쳤습니다.
    @Test
    void uploadFile() throws IOException {
        // given
        String path = "test";
        String fileName = "test-ocr.png";
        MultipartFile file = MultipartFileUtil.convertToMultipartFile("src/test/resources/" + fileName);

        // when
        String url = simpleStorageService.uploadFile(file, path);
//        simpleStorageService.deleteFile(url);

        // then
        assertNotNull(url);
        assertTrue(url.contains(bucket));
        assertTrue(url.contains(path));
        System.out.println("업로드된 URL: " + url);
//        System.out.println("삭제된 URL " + url);
    }

    // test 를 위한 MultipartFileUtil 클래스
    static class MultipartFileUtil {
        public static MultipartFile convertToMultipartFile(String filePath) throws IOException {
            File file = new File(filePath);
            FileInputStream input = new FileInputStream(file);
            return new MockMultipartFile("file", file.getName(), "image/jpg", input);
        }
    }
}