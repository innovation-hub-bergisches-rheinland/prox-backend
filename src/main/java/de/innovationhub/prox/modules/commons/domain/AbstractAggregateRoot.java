package de.innovationhub.prox.modules.commons.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAggregateRoot {

  private final transient @Transient List<DomainEvent> domainDomainEvents = new ArrayList<>();

  @CreatedDate
  private Instant createdAt;

  @LastModifiedDate
  private Instant modifiedAt;

  @DomainEvents
  @Transient
  public List<DomainEvent> getDomainEvents() {
    return List.copyOf(domainDomainEvents);
  }

  protected void registerEvent(DomainEvent domainEvent) {
    Objects.requireNonNull(domainEvent);

    domainDomainEvents.add(domainEvent);
  }

  @AfterDomainEventPublication
  protected void clearEvents() {
    domainDomainEvents.clear();
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getModifiedAt() {
    return modifiedAt;
  }
}
