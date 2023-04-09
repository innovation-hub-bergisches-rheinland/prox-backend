package de.innovationhub.prox.modules.recommendation.domain.calc;

import static org.junit.jupiter.api.Assertions.*;

import de.innovationhub.prox.modules.recommendation.domain.calc.JaccardIndexCalculator;
import java.util.List;
import org.junit.jupiter.api.Test;

class JaccardIndexCalculatorTest {

  JaccardIndexCalculator calculator = new JaccardIndexCalculator();

  @Test
  void testCalculate() {
    assertEquals(0.0, calculator.calculate(List.of(), List.of()));
    assertEquals(0.0, calculator.calculate(List.of(1), List.of()));
    assertEquals(0.0, calculator.calculate(List.of(), List.of(1)));
    assertEquals(0.0, calculator.calculate(List.of(1), List.of(2)));

    assertEquals(1.0, calculator.calculate(List.of(1), List.of(1)));
    assertEquals(0.5, calculator.calculate(List.of(1, 2), List.of(1)));
    assertEquals(0.5, calculator.calculate(List.of(1), List.of(1, 2)));
  }

}