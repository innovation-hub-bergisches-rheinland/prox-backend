package de.innovationhub.prox.modules.recommendation.domain.calc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.innovationhub.prox.modules.recommendation.domain.calc.OverlapCoefficientCalculator;
import java.util.List;
import org.junit.jupiter.api.Test;

class OverlapCoefficientCalculatorTest {

  OverlapCoefficientCalculator calculator = new OverlapCoefficientCalculator();

  @Test
  void testCalculate() {
    assertEquals(0.0, calculator.calculate(List.of(), List.of()));
    assertEquals(0.0, calculator.calculate(List.of(1), List.of()));
    assertEquals(0.0, calculator.calculate(List.of(), List.of(1)));
    assertEquals(0.0, calculator.calculate(List.of(1), List.of(2)));

    assertEquals(1.0, calculator.calculate(List.of(1), List.of(1)));
    assertEquals(1.0, calculator.calculate(List.of(1, 2), List.of(1)));
    assertEquals(1.0, calculator.calculate(List.of(1), List.of(1, 2)));

    assertEquals(0.5, calculator.calculate(List.of(1, 9), List.of(1, 2, 3, 4)));
  }

}