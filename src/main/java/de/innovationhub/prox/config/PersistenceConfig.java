package de.innovationhub.prox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class PersistenceConfig {

  // We use a prefix for all schemas to avoid conflicts with other schemas and reserved keywords
  private static final String SCHEMA_PREFIX = "prox_";
  public static final String PROJECT_SCHEMA = SCHEMA_PREFIX + "project";
  public static final String ORGANIZATION_SCHEMA = SCHEMA_PREFIX + "organization";
  public static final String STAR_SCHEMA = SCHEMA_PREFIX + "star";
  public static final String TAG_SCHEMA = SCHEMA_PREFIX + "tag";
  public static final String USER_SCHEMA = SCHEMA_PREFIX + "user";
}
