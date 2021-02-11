package edu.cs3500.spreadsheets.model;

/**
 * Represents a Worksheet made of a grid of cells that can be read from and its contents can be
 * changed.
 */
public interface Worksheet extends ReadableWorksheet {
  /**
   * Adds new cell to the Worksheet at the given coordinate and with containing the given content.
   * If there is already a cell at the coordinate, replaces it with a new cell. Also reevaluates all
   * cells to make sure there is no new errors.
   * @param coord   the location in the worksheet
   * @param content the content of the new cell
   */
  void addCell(Coord coord, String content);

  /**
   * Modifies the cell iff it exists. Also reevaluates all cells to make sure that there are no
   * new errors.
   * @param coord the location of the cell
   * @param content the new content
   */
  void modifyCell(Coord coord, String content);

  /**
   * Deletes the cell iff it exists. Also reevaluates all cells to make sure that there are no
   * new errors.
   * @param cell the location of the cell
   */
  void deleteCell(Coord cell);
}
