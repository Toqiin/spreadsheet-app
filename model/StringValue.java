package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents a string cell value.
 */
public class StringValue implements SValue {

  private String str;

  /**
   * Constructs a StringValue with the given value.
   * @param str the value
   */
  public StringValue(String str) {
    this.str = str;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StringValue that = (StringValue) o;
    return Objects.equals(str, that.str);
  }

  @Override
  public int hashCode() {
    return Objects.hash(str);
  }

  @Override
  public String toString() {
    String result = this.str.replace("\"","\\\"");
    result = result.replace("\\", "\\\\");
    return "\"" + result + "\"";
  }
}
