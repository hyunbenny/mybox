package com.hyunbenny.mybox.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileManager {

    @Value("${application.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public String uploadFile(String folderName, String saveFileName, MultipartFile multipartFile) throws IOException, Exception {
        ObjectMetadata objMetadata = new ObjectMetadata();
        objMetadata.setContentLength(multipartFile.getInputStream().available());

        createFolder(folderName + "/");

        // 파일 업로드
        s3Client.putObject(bucketName, saveFileName, multipartFile.getInputStream(), objMetadata);

        return s3Client.getUrl(bucketName, saveFileName).toString();
    }

    public byte[] downloadFile(String storeFileName) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, storeFileName);
        return IOUtils.toByteArray(s3Object.getObjectContent());
    }

    public void deleteFile(String storeFileName) throws Exception{
        s3Client.deleteObject(bucketName, storeFileName);
    }

    private void createFolder(String folderName) {
        if(!existDir(folderName))
            s3Client.putObject(bucketName, folderName, new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
    }

    private boolean existDir(String folderName) {
        return s3Client.doesObjectExist(bucketName, folderName);
    }

}
