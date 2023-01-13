package de.innovationhub.prox.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {
  public static final String TOPIC_EXCHANGE_NAME = "amqp.topic";
  public static final String CREATE_GROUP_MEMBERSHIP_KEY = "KK.EVENT.ADMIN.*.SUCCESS.GROUP_MEMBERSHIP.CREATE";
  public static final String GROUP_ADDED_QUEUE_NAME = "prox-group-added";

  @Bean
  public Jackson2JsonMessageConverter jackson2MessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
