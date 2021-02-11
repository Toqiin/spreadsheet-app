package edu.cs3500.spreadsheets.view;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JComponent;

import edu.cs3500.spreadsheets.controller.SpreadsheetFeatures;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadableWorksheet;

/**
 * Represents a edu.cs3500.spreadsheets.view of a Worksheet.
 */
public interface SpreadsheetView {

  /**
   * Saves the spreadsheet to the given file.
   * @param dest the given file
   * @throws FileNotFoundException if the file cannot be found
   */
  void save(File dest) throws FileNotFoundException;

  /**
   * Makes sure that the cell is visible.
   * @param cell the coordinate of the cell
   */
  void showCell(Coord cell);

  /**
   * Renders the edu.cs3500.spreadsheets.view from the Worksheet.
   */
  void render();

  /**
   * Returns the ReadableWorksheet that the edu.cs3500.spreadsheets.view has.
   * @return the worksheet
   */
  ReadableWorksheet getWorksheet();

  /**
   * Adds the features enabled by the edu.cs3500.spreadsheets.controller.
   * @param features the edu.cs3500.spreadsheets.controller
   */
  void addFeatures(SpreadsheetFeatures features);

  JComponent getMain();
}
