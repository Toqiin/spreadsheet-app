package edu.cs3500.spreadsheets.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents a double cell value.
 */
public class DoubleValue implements SValue {

  private double num;

  /**
   * Constructs a DoubleValue with the given value.
   * @param num the value
   */
  public DoubleValue(double num) {
    this.num = num;
  }

  /**
   * Gets the value of this DoubleValue.
   * @return the value of this
   */
  public double getValue() {
    return this.num;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DoubleValue that = (DoubleValue) o;
    return Double.compare(that.round(5), this.round(5)) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.round(5));
  }

  private double round(int roundTo) {
    if (roundTo < 0) {
      throw new IllegalArgumentException();
    }
    BigDecimal dec = new BigDecimal(Double.toString(this.num));
    dec = dec.setScale(roundTo, RoundingMode.HALF_UP);
    return dec.doubleValue();
  }

  @Override
  public String toString() {
    return String.format("%f", this.num);
  }
}
