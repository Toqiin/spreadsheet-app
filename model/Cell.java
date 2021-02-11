package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;

/**
 * Represents a cell that contains data that can be read and set.
 * A cell can contain a value (boolean, double, or string)
 * or a formula (value, reference, or function).
 */
public interface Cell {
  /**
   * Reads the cell and returns the corresponding Sexp.
   * @return the cell's contents as an Sexp
   * @throws IllegalArgumentException if the contents are not a properly formed Sexp
   */
  Sexp convertString() throws IllegalArgumentException;

  /**
   * Gets the contents of the cell.
   * @return the contents of the cell
   */
  String getString();

  /**
   * Sets the content of the cell to the given content.
   * @param content the new content of the cell
   */
  void setString(String content);

}
