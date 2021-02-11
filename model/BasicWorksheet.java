package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.cs3500.spreadsheets.sexp.Sexp;

/**
 * Represents a worksheet made of cells. Cells are stored with their coordinate in the worksheet.
 */
public class BasicWorksheet implements Worksheet {

  private Map<Coord, Cell> cellMap;
  private Map<Coord, SValue> evaluated;
  private Map<Coord, String> errors;

  /**
   * Constructs a basic worksheet with the given cells.
   * @param m the cells
   */
  private BasicWorksheet(Map<Coord, Cell> m) {
    this.cellMap = m;
    this.evaluated = new HashMap<>();
    this.errors = new HashMap<>();
  }

  @Override
  public Cell getCellAt(Coord coord) {
    return this.cellMap.getOrDefault(coord, null);
  }

  @Override
  public SValue evaluateCell(Cell cell, List<Cell> worklist, Map<Sexp, SValue> alreadyEval) {
    if (cell == null) {
      throw new IllegalArgumentException("Cannot reference empty cell");
    }
    if (worklist.contains(cell)) {
      throw new IllegalArgumentException();
    }
    Sexp s = cell.convertString();
    if (alreadyEval.containsKey(s)) {
      return alreadyEval.get(s);
    }
    worklist.add(cell);
    SValue val = s.accept(new EvalVisitor(this, worklist, alreadyEval));
    alreadyEval.put(s, val);
    return val;
  }

  @Override
  public SValue evaluateCell(Cell cell, Map<Sexp, SValue> alreadyEval) {
    Sexp s = cell.convertString();
    if (alreadyEval.containsKey(s)) {
      return alreadyEval.get(s);
    } else {
      SValue val = s.accept(new EvalVisitor(this,
              new ArrayList<>(Arrays.asList(cell)), alreadyEval));
      alreadyEval.put(s, val);
      return val;
    }
  }

  @Override
  public Cell getCell(String s) {
    Coord c = Coord.coordFromString(s);
    return this.getCellAt(c);
  }

  /**
   * Represents a builder for a BasicWorksheet that can add cells and create the worksheet.
   */
  public static class BasicWorksheetBuilder
          implements WorksheetReader.WorksheetBuilder<BasicWorksheet> {

    private final Map<Coord, Cell> cellMap;

    /**
     * Constructs a BasicWorksheetBuilder with no cells.
     */
    public BasicWorksheetBuilder() {
      this.cellMap = new HashMap<>();
    }

    @Override
    public WorksheetReader.WorksheetBuilder<BasicWorksheet> createCell(int col, int row,
                                                                       String contents) {
      cellMap.put(new Coord(col, row), new BasicCell(contents));
      return this;
    }

    @Override
    public BasicWorksheet createWorksheet() {
      return new BasicWorksheet(this.cellMap);
    }
  }

  @Override
  public Set<Coord> getAllNonEmptyCells() {
    return this.cellMap.keySet();
  }

  @Override
  public void addCell(Coord coord, String content) {
    Cell newCell = new BasicCell(content);
    this.cellMap.put(coord, newCell);
    this.evalAllCells();
  }

  @Override
  public void evalAllCells() {
    Set<Coord> cells = this.getAllNonEmptyCells();
    Map<Sexp, SValue> beenDone = new HashMap<>();
    for (Coord cell : cells) {
      try {
        SValue ignored = this.evaluateCell(this.getCellAt(cell), beenDone);
        this.evaluated.put(cell, ignored);
        beenDone.put(this.getCellAt(cell).convertString(), ignored);
      } catch (IllegalArgumentException e) {
        this.evaluated.remove(cell);
        this.errors.put(cell, "#ERROR!");
      }
    }
  }

  @Override
  public void modifyCell(Coord coord, String content) {
    if (this.cellMap.containsKey(coord)) {

      Cell cell = this.cellMap.get(coord);
      cell.setString(content);
      this.evalAllCells();

    } else {
      this.addCell(coord, content);
    }
  }

  @Override
  public void deleteCell(Coord cell) {
    this.cellMap.remove(cell);
    this.errors.remove(cell);
    this.evaluated.remove(cell);
    this.evalAllCells();
  }

  @Override
  public SValue getEvalFromCoord(Coord c) {
    return evaluated.getOrDefault(c, null);
  }

  @Override
  public String getErrorAtCoord(Coord c) {
    return errors.getOrDefault(c, null);
  }

  @Override
  public Map<Coord, String> getAllCellContents() {
    Map<Coord, String> result = new HashMap<>();
    for (Map.Entry<Coord, Cell> cell : this.cellMap.entrySet()) {
      result.put(cell.getKey(), cell.getValue().getString());
    }
    return result;
  }

}
