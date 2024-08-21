package io.bizbee.onechat.server.util;

import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

@Slf4j
@Component
public class MinioUtil {

  @Autowired
  private MinioClient minioClient;

  public Boolean bucketExists(String bucketName) {
    Boolean found;
    try {
      found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    } catch (Exception e) {
      log.error("bucket", e);
      return false;
    }
    return found;
  }

  public Boolean makeBucket(String bucketName) {
    try {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    } catch (Exception e) {
      log.error("bucket,", e);
      return false;
    }
    return true;
  }

  public Boolean setBucketPublic(String bucketName) {
    try {
      String sb =
          "{\"Version\":\"2012-10-17\"," + "\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":"
              + "{\"AWS\":[\"*\"]},\"Action\":[\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\","
              + "\"s3:GetBucketLocation\"],\"Resource\":[\"arn:aws:s3:::" + bucketName
              + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\"],\"Resource\":[\"arn:aws:s3:::"
              + bucketName + "/*\"]}]}";
      minioClient.setBucketPolicy(
          SetBucketPolicyArgs.builder().bucket(bucketName).config(sb).build());
    } catch (Exception e) {
      log.error("bucket,", e);
      return false;
    }
    return true;

  }

  public Boolean removeBucket(String bucketName) {
    try {
      minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    } catch (Exception e) {
      log.error("bucket,", e);
      return false;
    }
    return true;
  }

  public String upload(String bucketName, String path, MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    if (StringUtils.isBlank(originalFilename)) {
      throw new RuntimeException();
    }
    String fileName =
        System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
    String objectName =
        DateTimeUtils.getFormatDate(new Date(), DateTimeUtils.PARTDATEFORMAT) + "/" + fileName;
    try {
      PutObjectArgs objectArgs = PutObjectArgs.builder()
          .bucket(bucketName)
          .object(path + "/" + objectName)
          .stream(file.getInputStream(), file.getSize(), -1)
          .contentType(file.getContentType())
          .build();
      minioClient.putObject(objectArgs);
    } catch (Exception e) {
      log.error("error,", e);
      return null;
    }
    return objectName;
  }

  /**
   * @param bucketName  
   * @param path        
   * @param name        
   * @param fileByte    
   * @param contentType
   * @return Boolean
   */
  public String upload(String bucketName, String path, String name, byte[] fileByte,
      String contentType) {

    String fileName = System.currentTimeMillis() + name.substring(name.lastIndexOf("."));
    String objectName =
        DateTimeUtils.getFormatDate(new Date(), DateTimeUtils.PARTDATEFORMAT) + "/" + fileName;
    try {
      InputStream stream = new ByteArrayInputStream(fileByte);
      PutObjectArgs objectArgs = PutObjectArgs.builder()
          .bucket(bucketName)
          .object(path + "/" + objectName)
          .stream(stream, fileByte.length, -1)
          .contentType(contentType)
          .build();
      minioClient.putObject(objectArgs);
    } catch (Exception e) {
      log.error("error,", e);
      return null;
    }
    return objectName;
  }

  /**
   * @param bucketName 
   * @param fileName
   * @return
   * @throws Exception
   * @path path
   */
  public boolean remove(String bucketName, String path, String fileName) {
    try {
      minioClient.removeObject(
          RemoveObjectArgs.builder().bucket(bucketName).object(path + fileName).build());
    } catch (Exception e) {
      log.error("error,", e);
      return false;
    }
    return true;
  }
}
