package de.innovationhub.prox.infra.storage;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class S3StorageProvider implements StorageProvider {
  private final AmazonS3Client s3;
  private final String bucket;

  public S3StorageProvider(AmazonS3Client s3, AwsConfigurationProperties awsConfig) {
    this.s3 = s3;
    this.bucket = awsConfig.s3().bucket();

    if(!s3.doesBucketExistV2(bucket)) {
      s3.createBucket(bucket);
    }
  }

  @Override
  public void storeFile(String fileId, byte[] file, String contentType) {
    var metadata = new ObjectMetadata();
    metadata.setContentType(contentType);
    metadata.setContentLength(file.length);
    s3.putObject(bucket, fileId, new ByteArrayInputStream(file), metadata);
  }

  @Override
  public byte[] getFile(String fileId) throws IOException {
    var object = s3.getObject(bucket, fileId);
    return object.getObjectContent().readAllBytes();
  }

  @Override
  public void deleteFile(String fileId) {
    s3.deleteObject(bucket, fileId);
  }

  @Override
  public String buildUrl(String fileId) {
    return s3.getResourceUrl(bucket, fileId);
  }
}
