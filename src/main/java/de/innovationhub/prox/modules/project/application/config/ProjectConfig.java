package de.innovationhub.prox.modules.project.application.config;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.convert.DurationUnit;

@ConfigurationProperties(prefix = "project")
public record ProjectConfig(@DefaultValue ProjectJobConfig jobs) {
  public record ProjectJobConfig(
      @DefaultValue AutoArchiveConfig autoArchive,
      @DefaultValue AutoMarkForDeletionConfig autoMarkForDelete,
      @DefaultValue AutoDeletionConfig autoDelete) {

    public record AutoArchiveConfig(
        @DefaultValue("true") Boolean enable,
        @DurationUnit(ChronoUnit.DAYS) @DefaultValue("90") Duration after,
        @DefaultValue("0 0 0 * * *") String cron) {}

    public record AutoMarkForDeletionConfig(
        @DefaultValue("true") Boolean enable,
        @DurationUnit(ChronoUnit.DAYS) @DefaultValue("90") Duration after,
        @DefaultValue("0 0 0 * * *") String cron) {}

    public record AutoDeletionConfig(
        @DefaultValue("true") Boolean enable, @DefaultValue("0 0 0 * * *") String cron) {}
  }
}
