package de.innovationhub.prox.infra.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws")
public record AwsConfigurationProperties(AwsCredentials credentials, String region, S3Config s3) {
  public record AwsCredentials(String accessKey, String secretKey) {}
  public record S3Config(String bucket, String endpoint) {}
}
