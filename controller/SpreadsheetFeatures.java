package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.view.SpreadsheetView;

/**
 * Represents a edu.cs3500.spreadsheets.controller for a spreadsheet that allows the user to select cells and modify the
 * contents of cells.
 */
public interface SpreadsheetFeatures {

  /**
   * Adds the features of the edu.cs3500.spreadsheets.controller to the given edu.cs3500.spreadsheets.view.
   * @param view the edu.cs3500.spreadsheets.view
   */
  void setView(SpreadsheetView view);

  /**
   * Sets the contents of the cell at the given coordinate to the given content. If the cell does
   * not exist, then creates a new cell. If the given function is blank, deletes the cell.
   * @param cell the cell to be modified
   * @param function the new contents of the cell
   */
  void setString(Coord cell, String function);

  /**
   * Deletes the cell at the given coordinate from the worksheet.
   * @param cell the cell to be deleted
   */
  void deleteCell(Coord cell);
}
