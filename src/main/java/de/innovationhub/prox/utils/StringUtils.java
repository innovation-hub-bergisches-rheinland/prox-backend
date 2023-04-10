package de.innovationhub.prox.utils;

import java.text.Normalizer;

public class StringUtils {

  public static String slugify(String input) {
    return Normalizer.normalize(
            input.trim().toLowerCase(), Normalizer.Form.NFD
        )
        .replaceAll("[\\u0300-\\u036f]", "")
        // replace spaces with dashes
        .replaceAll("\\s+", "-")
        // remove duplicate dashes
        .replaceAll("-{2,}", "-");
  }

  private StringUtils() {
    throw new IllegalStateException("Utility class");
  }
}
