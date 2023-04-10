package de.innovationhub.prox.infra.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "de.innovationhub.prox" })
@ConfigurationPropertiesScan
public class SpringBootTestApplication {
  public static void main(String[] args) {
    SpringApplication.run(SpringBootTestApplication.class, args);
  }
}
