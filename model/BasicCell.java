package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;

/**
 * A concrete cell that contains data that can be read and set.
 */
public class BasicCell implements Cell {

  private String content;

  /**
   * Constructs a basic cell which contains the given piece of data.
   * @param content the contents of the cell
   */
  public BasicCell(String content) {
    this.content = content;
  }

  @Override
  public Sexp convertString() throws IllegalArgumentException {
    if (this.content.charAt(0) == '=') {
      return Parser.parse(this.content.substring(1));
    } else {
      return Parser.parse(this.content);
    }
  }

  @Override
  public void setString(String content) {
    this.content = content;
  }

  @Override
  public String getString() {
    return this.content;
  }

}
