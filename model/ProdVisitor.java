package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * Represents a visitor that attempts to multiply different types of SExps.
 * Only multiplies Doubles and will evaluate formulas.
 */
public class ProdVisitor extends SumVisitor implements SexpVisitor<Double> {

  /**
   * Constructs a ProdVisitor with the given worksheet, worklist of cells, and maps of already
   * evaluated Sexps and their evaluated SValues.
   * @param wkst   the worksheet
   * @param cells       the cells that have already been seen during evaluation
   * @param alreadyEval the SExps that have already been seen during evaluation and their
   *                    evaluated SValues
   */
  public ProdVisitor(ReadableWorksheet wkst, List<Cell> cells, Map<Sexp, SValue> alreadyEval) {
    super(wkst, cells, alreadyEval);
  }

  @Override
  public Double visitSymbol(String s) {
    SValue curr = this.alreadyEval.getOrDefault(new SSymbol(s), null);
    if (curr instanceof DoubleValue) {
      return ((DoubleValue) curr).getValue();
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
        return 1.0;
      }
      Sexp s1 = cell.convertString();
      List<Cell> copy = new ArrayList<>(this.cells);
      copy.add(cell);
      Double result =  s1.accept(new ProdVisitor(this.worksheet, copy, this.alreadyEval));
      this.alreadyEval.put(new SSymbol(s), new DoubleValue(result));
      return result;

    } else if (Coord.isMulti(s)) {
      Sexp sexp = new SList(new SSymbol("PRODUCT"), new SSymbol(s));
      SValue sVal = this.alreadyEval.getOrDefault(sexp, null);
      if (sVal instanceof DoubleValue) {
        return ((DoubleValue) sVal).getValue();
      }
      if (sVal != null) {
        throw new IllegalArgumentException();
      }
      int ind = s.indexOf(":");
      String left = s.substring(0, ind);
      String right = s.substring(ind + 1);
      List<SSymbol> cells = Coord.getCellsBetween(left, right);
      double d = 1;
      boolean allNull = true;
      for (SSymbol cell : cells) {
        allNull = allNull && (this.worksheet.getCell(cell.toString()) == null);
        d *= cell.accept(new ProdVisitor(worksheet, this.cells, this.alreadyEval));
      }
      if (allNull) {
        return 0.0;
      }
      this.alreadyEval.put(sexp, new DoubleValue(d));
      return d;

    } else if (Coord.isMultiCols(s)) {
      Sexp sexp = new SList(new SSymbol("PRODUCT"), new SSymbol(s));
      SValue sVal = this.alreadyEval.getOrDefault(sexp, null);
      if (sVal instanceof DoubleValue) {
        return ((DoubleValue) sVal).getValue();
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
      double d = 1;
      for (SSymbol cell : cells) {
        d *= cell.accept(new ProdVisitor(this.worksheet, this.cells, this.alreadyEval));
      }
      this.alreadyEval.put(sexp, new DoubleValue(d));
      return d;

    } else if (Coord.isValidColName(s)) {
      Sexp sexp = new SList(new SSymbol("PRODUCT"), new SSymbol(s));
      SValue sVal = this.alreadyEval.getOrDefault(sexp, null);
      if (sVal instanceof DoubleValue) {
        return ((DoubleValue) sVal).getValue();
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
      double d = 1;
      for (SSymbol cell : cells) {
        d *= cell.accept(new ProdVisitor(this.worksheet, this.cells, this.alreadyEval));
      }
      this.alreadyEval.put(sexp, new DoubleValue(d));
      return d;

    } else {
      throw new IllegalArgumentException();
    }
  }
}