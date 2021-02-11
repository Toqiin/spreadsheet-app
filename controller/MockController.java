package edu.cs3500.spreadsheets.controller;


import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.view.SpreadsheetView;

/**
 * Represents a mock version of a spreadsheet edu.cs3500.spreadsheets.controller that stores information about what action
 * is being performed. For testing the inputs to the edu.cs3500.spreadsheets.controller.
 */
public class MockController implements SpreadsheetFeatures {

  private String testString = "";

  @Override
  public void setView(SpreadsheetView view) {
    this.testString = Integer.toString(view.getWorksheet().getAllNonEmptyCells().size());
  }

  @Override
  public void setString(Coord cell, String function) {
    this.testString = cell.toString() + " " + function;
  }

  @Override
  public void deleteCell(Coord cell) {
    this.testString = cell.toString() +  " should be deleted.";
  }

  public String getString() {
    return this.testString;
  }
}
