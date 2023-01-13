package de.innovationhub.prox.infra.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

  public static final String TOPIC_EXCHANGE_NAME = "amq.topic";
  public static final String ERROR_EXCHANGE_NAME = "error";
  public static final String DEAD_LETTER_QUEUE_NAME = "dlt";
  public static final String CREATE_GROUP_MEMBERSHIP_KEY = "KK.EVENT.ADMIN.*.SUCCESS.GROUP_MEMBERSHIP.CREATE";
  public static final String VERIFY_EMAIL_KEY = "KK.EVENT.CLIENT.*.SUCCESS.*.VERIFY_EMAIL";
  public static final String GROUP_ADDED_QUEUE_NAME = "prox-group-added";
  public static final String VERIFY_EMAIL_QUEUE_NAME = "prox-verify-email";
  public static final String X_MESSAGE_TTL = "x-message-ttl";
  public static final String X_QUEUE_EXPIRES = "x-expires";
  public static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";

  @Bean
  public Jackson2JsonMessageConverter jackson2MessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public Exchange errorExchange() {
    return ExchangeBuilder.topicExchange(ERROR_EXCHANGE_NAME).durable(true).build();
  }

  @Bean
  public Queue deadLetterQueue() {
    return QueueBuilder.durable(DEAD_LETTER_QUEUE_NAME)
        .build();
  }

  @Bean
  public Binding deadLetterBinding() {
    return BindingBuilder.bind(deadLetterQueue())
        .to(errorExchange())
        .with("#")
        .noargs();
  }
}
