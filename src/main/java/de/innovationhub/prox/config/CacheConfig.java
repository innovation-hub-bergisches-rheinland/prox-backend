package de.innovationhub.prox.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  public static final String TAGS = "tags";
  public static final String ORGANIZATIONS = "organizations";
  public static final String USER_PROFILE = "userProfile";
  public static final String USERS = "users";
  public static final String USERS_ROLE = "users_role";
  public static final String USERS_SEARCH = "users_search";
  public static final String RECOMMENDATIONS = "recommendations";

  // TODO: Revisit Cache Configs
  @Bean
  public Caffeine caffeineConfig() {
    return Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES);
  }

  @Bean
  public CacheManager cacheManager(Caffeine caffeine) {
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
    caffeineCacheManager.setCaffeine(caffeine);
    caffeineCacheManager.setCacheNames(
        List.of(TAGS, ORGANIZATIONS, USER_PROFILE, USERS, USERS_ROLE, USERS_SEARCH, RECOMMENDATIONS));
    return caffeineCacheManager;
  }
}
