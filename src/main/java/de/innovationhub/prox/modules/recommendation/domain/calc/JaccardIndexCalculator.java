package de.innovationhub.prox.modules.recommendation.domain.calc;

import java.util.Collection;
import java.util.HashSet;

public class JaccardIndexCalculator {

  public <T> double calculate(Collection<T> setA, Collection<T> setB) {
    if (setA.isEmpty() || setB.isEmpty()) {
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