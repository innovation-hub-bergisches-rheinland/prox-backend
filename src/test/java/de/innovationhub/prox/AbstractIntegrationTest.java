package de.innovationhub.prox;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("test")
public class AbstractIntegrationTest {
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.2")
      .withDatabaseName("prox")
      .withUsername("prox")
      .withPassword("prox");
  static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
      .withServices(LocalStackContainer.Service.S3);

  static {
    postgres.start();
    localStack.start();
  }

  @DynamicPropertySource
  static void setLocalStack(DynamicPropertyRegistry registry) {
    registry.add("cloud.aws.credentials.accessKey", () -> localStack.getAccessKey());
    registry.add("cloud.aws.credentials.secretKey", () -> localStack.getSecretKey());
    registry.add("cloud.aws.region", () -> localStack.getRegion());
    registry.add("cloud.aws.s3.endpoint", () -> localStack.getEndpointOverride(Service.S3));
  }

  @DynamicPropertySource
  static void setDatabase(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }
}
