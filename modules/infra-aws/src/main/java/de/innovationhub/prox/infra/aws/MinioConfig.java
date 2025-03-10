package de.innovationhub.prox.infra.aws;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
//@ConditionalOnClass(AWSCredentials.class)
public class MinioConfig {
  private final AwsConfigurationProperties config;

  @Bean
  //@ConditionalOnClass(AmazonS3Client.class)
  public MinioClient minio() {
    return MinioClient.builder()
        .credentials(config.credentials().accessKey(), config.credentials().secretKey())
        .endpoint(config.s3().endpoint())
        .region("eu-central-1")
        .build();
  }
}
