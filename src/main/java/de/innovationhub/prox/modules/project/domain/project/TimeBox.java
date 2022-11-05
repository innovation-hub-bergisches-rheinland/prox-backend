package de.innovationhub.prox.modules.project.domain.project;

import java.time.LocalDate;
import javax.persistence.Embeddable;
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

  private LocalDate start;
  private LocalDate end;

  public TimeBox(LocalDate start, LocalDate end) {
    this.start = start;
    this.end = end;
  }
}
