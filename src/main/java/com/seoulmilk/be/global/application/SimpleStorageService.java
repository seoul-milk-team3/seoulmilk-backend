package com.seoulmilk.be.global.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.seoulmilk.be.global.exception.FileConvertFailException;
import com.seoulmilk.be.global.exception.errorcode.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleStorageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file, String path) {
        String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(file.getOriginalFilename());
        if (path.lastIndexOf("/") != path.length() - 1) {
            path += "/";
        }

        String filePath = path + fileName;
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            amazonS3.putObject(new PutObjectRequest(bucket, filePath, file.getInputStream(), metadata));
//                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return amazonS3.getUrl(bucket, filePath).toString();
        } catch (IOException e) {
            throw new FileConvertFailException(GlobalErrorCode.FILE_CONVERT_FAIL);
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files, String path) {
        return files.stream()
                .map(file -> uploadFile(file, path))
                .toList();
    }

    public void deleteFile(String path) {
        String fileName = path.replace("https://" + bucket + ".s3." + amazonS3.getRegionName() + ".amazonaws.com/", "");

        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
        }
    }
}
