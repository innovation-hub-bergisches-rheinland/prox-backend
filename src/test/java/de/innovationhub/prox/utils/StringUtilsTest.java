package de.innovationhub.prox.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StringUtilsTest {

  @Test
  void slugify() {
    assertEquals("", StringUtils.slugify(""));
    assertEquals("test", StringUtils.slugify("test"));
    assertEquals("test", StringUtils.slugify("TEST"));
    assertEquals("test", StringUtils.slugify("test "));
    assertEquals("test-test", StringUtils.slugify("test test"));
    assertEquals("test-test", StringUtils.slugify("test   test"));
    assertEquals("c#", StringUtils.slugify("C#"));
    assertEquals("c++", StringUtils.slugify("C++"));
  }
}