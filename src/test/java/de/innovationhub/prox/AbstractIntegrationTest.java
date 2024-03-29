package de.innovationhub.prox;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("test")
@ClearDatabase
public class AbstractIntegrationTest {

  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1-alpine")
      .withDatabaseName("prox")
      .withUsername("prox")
      .withPassword("prox");

  static LocalStackContainer localStack = new LocalStackContainer(
      DockerImageName.parse("localstack/localstack:1.3"))
      .withServices(LocalStackContainer.Service.S3);

  static RabbitMQContainer rabbitMQ = new RabbitMQContainer("rabbitmq:3.9.5-alpine");

  static {
    Startables.deepStart(
        postgres,
        localStack,
        rabbitMQ
    ).join();
  }

  @DynamicPropertySource
  static void setLocalStack(DynamicPropertyRegistry registry) {
    registry.add("cloud.aws.credentials.accessKey", localStack::getAccessKey);
    registry.add("cloud.aws.credentials.secretKey", localStack::getSecretKey);
    registry.add("cloud.aws.region", localStack::getRegion);
    registry.add("cloud.aws.s3.endpoint", () -> localStack.getEndpointOverride(Service.S3));
  }

  @DynamicPropertySource
  static void setDatabase(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @DynamicPropertySource
  static void setRabbitMQ(DynamicPropertyRegistry registry) {
    registry.add("spring.rabbitmq.host", () -> "localhost");
    registry.add("spring.rabbitmq.port", rabbitMQ::getAmqpPort);
    registry.add("spring.rabbitmq.username", rabbitMQ::getAdminUsername);
    registry.add("spring.rabbitmq.password", rabbitMQ::getAdminPassword);
  }
}
