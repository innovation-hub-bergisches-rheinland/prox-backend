package de.innovationhub.prox.modules.recommendation.domain.calc;

import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;

public class JaccardIndexCalculator {

  public <T> double calculate(@Nullable final Collection<T> setA,
      @Nullable final Collection<T> setB) {
    if (setA == null || setB == null || setA.isEmpty() || setB.isEmpty()) {
      return 0.0;
    }

    var sa = new HashSet<>(setA);
    var sb = new HashSet<>(setB);

    final var saSize = sa.size();
    final var sbSize = sb.size();

    sa.retainAll(sb);
    final var intersection = sa.size();
    return (double) intersection / (saSize + sbSize - intersection);
  }
}
