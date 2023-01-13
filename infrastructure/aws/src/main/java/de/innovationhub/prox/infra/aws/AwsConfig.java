package de.innovationhub.prox.infra.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {
  private final AwsConfigurationProperties config;

  @Bean
  @ConditionalOnMissingBean
  public AWSCredentials awsCredentials() {
    return new BasicAWSCredentials(config.credentials().accessKey(), config.credentials()
        .secretKey());
  }

  @Bean
  @ConditionalOnMissingBean
  public AmazonS3Client s3(AWSCredentials credentials) {
    return (AmazonS3Client) AmazonS3ClientBuilder
        .standard()
        .withEndpointConfiguration(new EndpointConfiguration(config.s3().endpoint(), config.region()))
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .build();
  }
}
