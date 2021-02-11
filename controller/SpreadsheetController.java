package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.view.SpreadsheetView;

/**
 * Represents an implementation of a edu.cs3500.spreadsheets.controller for a spreadsheet that allows the user to select
 * cells and modify the contents of cells.
 */
public class SpreadsheetController implements SpreadsheetFeatures {

  private Worksheet worksheet;
  private SpreadsheetView view;

  /**
   * Constructs a SpreadsheetController.
   * @param worksheet the worksheet that will be viewed and modified by the user
   */
  public SpreadsheetController(Worksheet worksheet) {
    this.worksheet = worksheet;
  }

  @Override
  public void setView(SpreadsheetView v) {
    this.view = v;
    this.view.addFeatures(this);
  }

  @Override
  public void setString(Coord cell, String function) {
    if (function.equals("")) {
      this.deleteCell(cell);
    } else {
      this.worksheet.modifyCell(cell, function);
      this.view.getMain().repaint();
    }

  }

  @Override
  public void deleteCell(Coord cell) {
    this.worksheet.deleteCell(cell);
    this.view.getMain().repaint();
  }

}
