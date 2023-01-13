package de.innovationhub.prox.infra.aws.s3.storage;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

public class AbstractLocalStackTest {

  static LocalStackContainer localStack = new LocalStackContainer(
      DockerImageName.parse("localstack/localstack:1.3"))
      .withServices(LocalStackContainer.Service.S3);

  static {
    localStack.start();
  }

  @DynamicPropertySource
  static void setLocalStack(DynamicPropertyRegistry registry) {
    registry.add("cloud.aws.credentials.accessKey", () -> localStack.getAccessKey());
    registry.add("cloud.aws.credentials.secretKey", () -> localStack.getSecretKey());
    registry.add("cloud.aws.region", () -> localStack.getRegion());
    registry.add("cloud.aws.s3.endpoint", () -> localStack.getEndpointOverride(Service.S3));
  }
}
