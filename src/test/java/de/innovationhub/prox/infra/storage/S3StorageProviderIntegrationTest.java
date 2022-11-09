package de.innovationhub.prox.infra.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import de.innovationhub.prox.AbstractIntegrationTest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

// TODO: We probably do not need to spin up spring boot at all. Re-evaluate test slices
class S3StorageProviderIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  S3StorageProvider s3StorageProvider;

  @Autowired
  AmazonS3 s3Client;

  @Autowired
  AwsConfigurationProperties config;

  @Test
  void shouldStoreBytes() throws IOException {
    var bytes = "test".getBytes();
    var fileId = UUID.randomUUID().toString();
    s3StorageProvider.storeFile(fileId, bytes, "text/plain");

    var storedBytes = s3Client.getObject(config.s3().bucket(), fileId).getObjectContent().readAllBytes();

    assertArrayEquals(bytes, storedBytes);
  }

  @Test
  void shouldSetContentType() {
    var bytes = "test".getBytes();
    var fileId = UUID.randomUUID().toString();
    s3StorageProvider.storeFile(fileId, bytes, "text/plain");
    var object = s3Client.getObject(config.s3().bucket(), fileId);

    assertThat(object.getObjectMetadata().getContentType())
        .isEqualTo("text/plain");
  }

  @Test
  void shouldReadFile() throws IOException {
    var content = "test";
    var fileId = UUID.randomUUID().toString();
    s3Client.putObject(config.s3().bucket(), fileId, content);

    var bytes = s3StorageProvider.getFile(fileId);

    assertThat(content).isEqualTo(new String(bytes));
  }

  @Test
  void shouldDeleteFile() {
    var content = "test";
    var fileId = UUID.randomUUID().toString();
    s3Client.putObject(config.s3().bucket(), fileId, content);

    s3StorageProvider.deleteFile(fileId);

    var exists = s3Client.doesObjectExist(config.s3().bucket(), fileId);
    assertThat(exists)
        .isFalse();
  }
}