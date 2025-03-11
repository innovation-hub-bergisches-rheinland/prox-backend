package de.innovationhub.prox.infra.aws;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Primary
@Component
public class MinioStorageProvider implements StorageProvider {

  private final MinioClient minio;
  private final String bucket;

  public MinioStorageProvider(MinioClient minio,
      AwsConfigurationProperties awsConfigurationProperties) {
    this.minio = minio;
    this.bucket = awsConfigurationProperties.s3().bucket();
    try {
      if (!minio.bucketExists(BucketExistsArgs.builder()
          .bucket(bucket).build())) {
        minio.makeBucket(MakeBucketArgs.builder()
            .bucket(bucket).build());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void storeFile(String fileId, byte[] file, String contentType) throws IOException {
    try {
      var stream = new ByteArrayInputStream(file);
      minio.putObject(PutObjectArgs
          .builder()
          .object(fileId)
          .contentType(contentType)
          .bucket(bucket)
          .stream(stream, stream.available(), -1)
          .build());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String buildUrl(String fileId) {
    try {
      return minio.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
          .expiry(2, TimeUnit.HOURS)
          .method(Method.GET)
          .object(fileId)
          .bucket(bucket).build());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
