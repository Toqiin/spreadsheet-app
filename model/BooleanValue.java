package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents a boolean cell value.
 */
public class BooleanValue implements SValue {

  private boolean bool;

  /**
   * Constructs a BooleanValue with the given value.
   * @param bool the value
   */
  public BooleanValue(boolean bool) {
    this.bool = bool;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)  {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BooleanValue that = (BooleanValue) o;
    return this.bool == that.bool;
  }

  @Override
  public int hashCode() {
    return Objects.hash(bool);
  }

  @Override
  public String toString() {
    return Boolean.toString(this.bool);
  }
}
