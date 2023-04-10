package de.innovationhub.prox.infra.aws;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.amazonaws.services.s3.AmazonS3;
import de.innovationhub.prox.infra.aws.AwsConfigurationProperties;
import de.innovationhub.prox.infra.aws.S3StorageProvider;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

// TODO: We probably do not need to spin up spring boot at all. Re-evaluate test slices
@SpringBootTest
class S3StorageProviderIntegrationTest {
  @Autowired
  S3StorageProvider s3StorageProvider;

  @Autowired
  AmazonS3 s3Client;

  @Autowired
  AwsConfigurationProperties config;

  static LocalStackContainer localStack = new LocalStackContainer(
      DockerImageName.parse("localstack/localstack:1.3"))
      .withServices(LocalStackContainer.Service.S3);

  static {
    localStack.start();
  }

  @DynamicPropertySource
  static void setLocalStack(DynamicPropertyRegistry registry) {
    registry.add("cloud.aws.credentials.accessKey", localStack::getAccessKey);
    registry.add("cloud.aws.credentials.secretKey", localStack::getSecretKey);
    registry.add("cloud.aws.region", localStack::getRegion);
    registry.add("cloud.aws.s3.endpoint", () -> localStack.getEndpointOverride(Service.S3));
  }


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
  void shouldGetUrl() {
    var content = "test";
    var fileId = UUID.randomUUID().toString();
    s3Client.putObject(config.s3().bucket(), fileId, content);

    var url = s3StorageProvider.buildUrl(fileId);

    assertThat(url)
        .startsWith("http://")
        .endsWith(fileId);
  }
}