package de.innovationhub.prox.utils;

import java.text.Normalizer;

public class StringUtils {

  private StringUtils() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Slugs a string.
   *
   * @param input string to slug
   * @return slugged string
   */
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
}
