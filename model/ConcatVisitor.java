package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * Represents a visitor that attempts to concatenate different types of SExps.
 * Only concatenates Strings and will evaluate formulas.
 */
public class ConcatVisitor implements SexpVisitor<String> {

  private ReadableWorksheet worksheet;
  private List<Cell> cells;
  private Map<Sexp, SValue> alreadyEval;

  /**
   * Constructs a ConcatVisitor with a worksheet and worklist of cells.
   * @param worksheet the worksheet
   * @param cells     the cells that have already been seen when evaluating
   */
  public ConcatVisitor(ReadableWorksheet worksheet, List<Cell> cells, Map<Sexp,
          SValue> alreadyEval) {
    this.worksheet = worksheet;
    this.cells = cells;
    this.alreadyEval = alreadyEval;
  }

  @Override
  public String visitBoolean(boolean b) {
    throw new IllegalArgumentException();
  }

  @Override
  public String visitNumber(double d) {
    throw new IllegalArgumentException();
  }

  @Override
  public String visitSList(List<Sexp> l) {
    if (l == null || l.size() <= 1) {
      throw new IllegalArgumentException();
    }
    Sexp function = l.get(0);
    if (function.equals(new SSymbol("CONCAT"))) {
      StringBuilder str = new StringBuilder();
      for (int i = 1; i < l.size(); i += 1) {
        str.append(l.get(i).accept(new ConcatVisitor(this.worksheet, this.cells,
                this.alreadyEval)));
      }
      return str.toString();
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public String visitSymbol(String s) {
    SValue curr = this.alreadyEval.getOrDefault(new SSymbol(s), null);
    if (curr instanceof StringValue) {
      return curr.toString();
    }
    if (curr != null) {
      throw new IllegalArgumentException();
    }

    if (Coord.validCellName(s)) {
      Cell cell = this.worksheet.getCell(s);
      if (this.cells.contains(cell)) {
        throw new IllegalArgumentException();
      }
      if (cell == null) {
        return "";
      }
      Sexp s1 = cell.convertString();
      List<Cell> copy = new ArrayList<>(this.cells);
      copy.add(cell);
      String result =  s1.accept(new ConcatVisitor(this.worksheet, copy, this.alreadyEval));
      this.alreadyEval.put(new SSymbol(s), new StringValue(result));
      return result;

    } else if (Coord.isMulti(s)) {
      Sexp sexp = new SList(new SSymbol("CONCAT"), new SSymbol(s));
      SValue sVal = this.alreadyEval.getOrDefault(sexp, null);
      if (sVal instanceof StringValue) {
        return sVal.toString();
      }
      if (sVal != null) {
        throw new IllegalArgumentException();
      }
      int ind = s.indexOf(":");
      String left = s.substring(0, ind);
      String right = s.substring(ind + 1);
      List<SSymbol> cells = Coord.getCellsBetween(left, right);
      StringBuilder str = new StringBuilder();
      for (SSymbol cell : cells) {
        str.append(cell.accept(new ConcatVisitor(this.worksheet, this.cells, this.alreadyEval)));
      }
      this.alreadyEval.put(sexp, new StringValue(str.toString()));
      return str.toString();

    } else if (Coord.isValidColName(s)) {
      Sexp sexp = new SList(new SSymbol("PRODUCT"), new SSymbol(s));
      SValue sVal = this.alreadyEval.getOrDefault(sexp, null);
      if (sVal instanceof StringValue) {
        return sVal.toString();
      } else if (sVal != null) {
        throw new IllegalArgumentException();
      }
      List<SSymbol> cells = new ArrayList<>();
      int colIndex = Coord.colNameToIndex(s);
      for (Coord c : worksheet.getAllNonEmptyCells()) {
        if (c.col == colIndex) {
          if (this.cells.contains(this.worksheet.getCellAt(c))) {
            throw new IllegalArgumentException("Cycle detected");
          } else {
            cells.add(new SSymbol(c.toString()));
          }
        }
      }
      StringBuilder str = new StringBuilder();
      for (SSymbol cell : cells) {
        str.append(cell.accept(new ConcatVisitor(this.worksheet, this.cells, this.alreadyEval)));
      }
      this.alreadyEval.put(sexp, new StringValue(str.toString()));
      return str.toString();

    } else if (Coord.isMultiCols(s)) {
      Sexp sexp = new SList(new SSymbol("PRODUCT"), new SSymbol(s));
      SValue sVal = this.alreadyEval.getOrDefault(sexp, null);
      if (sVal instanceof StringValue) {
        return sVal.toString();
      }
      if (sVal != null) {
        throw new IllegalArgumentException();
      }
      int ind = s.indexOf(":");
      String left = s.substring(0, ind);
      String right = s.substring(ind + 1);
      List<Integer> colIndexes = Coord.getColsBetween(left, right);
      List<SSymbol> cells = new ArrayList<>();
      for (Coord c : worksheet.getAllNonEmptyCells()) {
        if (colIndexes.contains(c.col)) {
          if (this.cells.contains(this.worksheet.getCellAt(c))) {
            throw new IllegalArgumentException("Cycle detected");
          } else {
            cells.add(new SSymbol(c.toString()));
          }
        }
      }
      StringBuilder str = new StringBuilder();
      for (SSymbol cell : cells) {
        str.append(cell.accept(new ConcatVisitor(this.worksheet, this.cells, this.alreadyEval)));
      }
      this.alreadyEval.put(sexp, new StringValue(str.toString()));
      return str.toString();

    } else {
      throw new IllegalArgumentException("Must be a cell reference");
    }
  }

  @Override
  public String visitString(String s) {
    return s;
  }
}
