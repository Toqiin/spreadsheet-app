package edu.cs3500.spreadsheets.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.cs3500.spreadsheets.sexp.Sexp;

/**
 * Represents a Worksheet made of a grid of cells that can only be read from.
 */
public interface ReadableWorksheet {

  /**
   * Returns the cell at the given Coord in the worksheet. Rows and columns in the worksheet start
   * counting at 1.
   * @param coord the coord of the desired cell
   * @return the cell located at the given Coord in the Worksheet, or null if the cell is empty
   */
  Cell getCellAt(Coord coord);

  /**
   * Returns the cell with the given name in the worksheet. Cell names contain the column which
   * follows the pattern:
   * Columns 1 through 26 get names A through Z
   * Columns 27 through 52 get names AA through AZ
   * ...
   * and the row which starts counting from 1.
   * @param s the name of the cell
   * @return the cell located at the given Coord in the Worksheet, or null if the cell is empty
   */
  Cell getCell(String s);

  /**
   * Evaluates the given cell and returns the value of the cell, while making sure that if the cell
   * contains a formula, the formula does not reference to itself. Also keeps track of SExp that
   * have already been evaluated, and their evaluated values.
   * @param cell the cell to be evaluated
   * @param alreadyEval the map of SExps that have already been evaluated and their evaluated
   *                    SValues
   * @return the evaluated value of the cell
   */
  SValue evaluateCell(Cell cell, Map<Sexp, SValue> alreadyEval);

  /**
   * Evaluates the given cell and returns the value of the cell. Makes sure that if the cell
   * contains a formula, the formula does not reference to itself and that the cell has not already
   * been looked at (in the given list). Also keeps track of SExp that have already been evaluated,
   * and their evaluated values.
   * @param cell  the cell to be evaluated
   * @param cells the cells that have already been seen when evaluating
   * @param alreadyEval the map of SExps that have already been evaluated and their evaluated
   *                    SValues
   * @return the evaluated value of the cell
   */
  SValue evaluateCell(Cell cell, List<Cell> cells, Map<Sexp, SValue> alreadyEval);

  /**
   * Returns a set of the coordinates of all non-empty cells in the Worksheet.
   * @return the coordinates of all non-empty cells in the worksheet
   */
  Set<Coord> getAllNonEmptyCells();

  /**
   * Finds the evaluated value of the cell that is located in the Worksheet at the given Coord.
   * If the cell cannot be evaluated because of an error, returns null.
   * @param c the coordinate of the cell in the grid
   * @return the evaluated value of the cell
   */
  SValue getEvalFromCoord(Coord c);

  /**
   * If the cell cannot be evaluated because of an error, finds the error message corresponding to
   * the cell at the given Coord.
   * @param c the coordinate of the cell in the grid
   * @return the error message of the cell
   */
  String getErrorAtCoord(Coord c);

  /**
   * Finds the contents of all the non-empty cells in the worksheet.
   * @return a map of the coordinate of the cell and the cell's contents
   */
  Map<Coord, String> getAllCellContents();

  /**
   * Helper function that throws an error if all cells cannot evaluate. Should only be called when
   * the contents of a cell are updated or when a spreadsheet is initialized.
   */
  void evalAllCells();
}
