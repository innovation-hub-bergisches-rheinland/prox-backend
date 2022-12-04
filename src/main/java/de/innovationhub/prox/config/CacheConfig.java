package de.innovationhub.prox.config;

import java.util.List;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String TAGS = "tags";
    public static final String ORGANIZATIONS = "organizations";
    public static final String LECTURERS = "lecturers";
    public static final String USERS = "users";

    // TODO: Use Redis instead of ConcurrentMapCacheManager
    @Component
    public static class SimpleCacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

      @Override
      public void customize(ConcurrentMapCacheManager cacheManager) {
        cacheManager.setCacheNames(List.of(TAGS, ORGANIZATIONS, LECTURERS, USERS));
      }
    }
}
