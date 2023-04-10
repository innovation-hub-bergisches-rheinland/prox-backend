package de.innovationhub.prox.utils;

public class StringUtils {
  public static String slugify(String input) {
    return input.trim()
        .toLowerCase()
        // replace spaces with dashes
        .replaceAll(" ", "-")
        // remove duplicate dashes
        .replaceAll("-{2,}", "-");
  }
}
