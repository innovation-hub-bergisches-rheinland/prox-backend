package de.innovationhub.prox.modules.project.domain.project;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Period where a project is actively worked on. It is meant to be defined vaguely, as sometimes a
 * lecturer might not have the capacity to carry out a project in a specific time frame. But we must
 * have some kind of time frame to be able to accumulate the project's progress.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TimeBox {

  private LocalDate startDate;
  private LocalDate endDate;

  public TimeBox(LocalDate startDate, LocalDate endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
