package org.springframe.backend.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class fileUploadUtils {
    private final MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    public String upload(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();

        String name = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".") ) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String objectName = name + fileExtension;

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                        .bucket(bucketName)
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                            .bucket(bucketName)
                    .build());
        }

        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(inputStream, inputStream.available(), -1)
                .contentType(file.getContentType())
                .build();
        minioClient.putObject(args);

        String presignedUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(60 * 60 * 24)
                        .build()
        );
        return presignedUrl;


    }

    public List<String> listFiles(){
        List<String> fileNames = new ArrayList<>();
        try{
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .recursive(true)
                            .build()
            );
            for (Result<Item> result : results) {
                Item item = result.get();
                fileNames.add(item.objectName());
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
     return fileNames;
    }

    public boolean deleteFiles(List<String> fileNames){
        try{
            List<DeleteObject> objectToDelete = fileNames.stream()
                    .map(DeleteObject::new)
                    .toList();
            RemoveObjectsArgs args = RemoveObjectsArgs.builder()
                    .bucket(bucketName)
                    .objects(objectToDelete)
                    .build();
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(args);
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                log.error("Error deleting object: {}", error.objectName());
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

}
