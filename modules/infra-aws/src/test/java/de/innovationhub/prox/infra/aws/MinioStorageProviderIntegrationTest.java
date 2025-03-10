package de.innovationhub.prox.infra.aws;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MinIOContainer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

// TODO: We probably do not need to spin up spring boot at all. Re-evaluate test slices
@Slf4j
@SpringBootTest
@ActiveProfiles("self-hosted")
class MinioStorageProviderIntegrationTest {
  @Autowired
  MinioStorageProvider s3StorageProvider;

  @Autowired
  MinioClient s3Client;

  @Autowired
  AwsConfigurationProperties config;
  //These are obviously just for testing
  static final String secretKey = "YLslAWJqEL9fBJTJpN0Lz6mHpjUQUnbt9azzDDrn";
  static final String accessKey = "tOEJwRwoCV6xZfOG3RrR";

  static MinIOContainer container = new MinIOContainer("minio/minio:RELEASE.2023-09-04T19-57-37Z")
        .withEnv("MINIO_ACCESS_KEY", accessKey)
      .withEnv("MINIO_SECRET_KEY", secretKey)
      .withExposedPorts(9000);

  static {
    container.start();
  }


  @DynamicPropertySource
  static void setMinio(DynamicPropertyRegistry registry) {
    container.getEnvMap().forEach((k, v) -> log.info("env: {}={}", k, v));
    registry.add("cloud.aws.credentials.accessKey", () -> accessKey);
    registry.add("cloud.aws.credentials.secretKey", () -> secretKey);
    registry.add("cloud.aws.s3.endpoint", () -> "http://" + container.getHost() + ":" + container.getMappedPort(9000));
  }

  @SneakyThrows
  @Test
  void shouldStoreBytes() throws IOException {
    var bytes = "test".getBytes();
    var fileId = UUID.randomUUID().toString();
    s3StorageProvider.storeFile(fileId, bytes, "text/plain");

    var storedBytes = s3Client.getObject(GetObjectArgs.builder()
        .object(fileId)
            .bucket(config.s3().bucket())
        .build()).readAllBytes();

    assertArrayEquals(bytes, storedBytes);
  }

  @SneakyThrows
  @Test
  void shouldSetContentType() throws IOException {
    var bytes = "test".getBytes();
    var fileId = UUID.randomUUID().toString();
    s3StorageProvider.storeFile(fileId, bytes, "text/plain");
    var header = s3Client.statObject(StatObjectArgs.builder()
        .object(fileId)
        .bucket(config.s3().bucket())
        .build()).contentType();

    assertThat(header)
        .isEqualTo("text/plain");
  }

  @SneakyThrows
  @Test
  void shouldGetUrl() {
    var content = "test";
    var buf = new ByteArrayInputStream(content.getBytes());
    var fileId = UUID.randomUUID().toString();
    s3Client.putObject(PutObjectArgs.builder()
        .object(fileId)
            .bucket(config.s3().bucket())
        .stream(buf, buf.available(), -1 ).build());

    var url = s3StorageProvider.buildUrl(fileId);

    assertThat(url)
        .startsWith("http://")
        .contains(fileId);
  }
}