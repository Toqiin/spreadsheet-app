package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * Represents a visitor that attempts to sum different types of SExps.
 * Only sums Doubles and will evaluate formulas.
 */
public class SumVisitor implements SexpVisitor<Double> {
  final ReadableWorksheet worksheet;
  final List<Cell> cells;
  final Map<Sexp, SValue> alreadyEval;

  /**
   * Constructs a SumVisitor with the given worksheet, worklist of cells, and maps of already
   * evaluated Sexps and their evaluated SValues.
   * @param worksheet   the worksheet
   * @param cells       the cells that have already been seen during evaluation
   * @param alreadyEval the SExps that have already been seen during evaluation and their
   *                    evaluated SValues
   */
  public SumVisitor(ReadableWorksheet worksheet, List<Cell> cells, Map<Sexp, SValue> alreadyEval) {
    this.worksheet = worksheet;
    this.cells = cells;
    this.alreadyEval = alreadyEval;
  }

  @Override
  public Double visitBoolean(boolean b) {
    throw new IllegalArgumentException();
  }

  @Override
  public Double visitNumber(double d) {
    return d;
  }

  @Override
  public Double visitSList(List<Sexp> l) {
    SValue curr = this.alreadyEval.getOrDefault(new SList(l), null);
    if (curr instanceof DoubleValue) {
      return ((DoubleValue) curr).getValue();
    }

    if (l == null || l.size() <= 1) {
      throw new IllegalArgumentException();
    }
    Sexp function = l.get(0);
    if (function.equals(new SSymbol("SUM"))) {
      double sum = 0;
      for (int i = 1; i < l.size(); i += 1 ) {
        sum += l.get(i).accept(new SumVisitor(this.worksheet, this.cells, this.alreadyEval));
      }
      this.alreadyEval.put(new SList(l), new DoubleValue(sum));
      return sum;
    } else if (function.equals(new SSymbol("PRODUCT"))) {
      double prod = 1;
      for (int i = 1; i < l.size(); i += 1 ) {
        prod *= l.get(i).accept(new ProdVisitor(this.worksheet, this.cells, this.alreadyEval));
      }
      this.alreadyEval.put(new SList(l), new DoubleValue(prod));
      return prod;
    } else {
      throw new IllegalArgumentException("Invalid function");
    }
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
        return 0.0;
      }
      Sexp s1 = cell.convertString();
      List<Cell> copy = new ArrayList<>(this.cells);
      copy.add(cell);

      Double result =  s1.accept(new SumVisitor(this.worksheet, copy, this.alreadyEval));
      this.alreadyEval.put(new SSymbol(s), new DoubleValue(result));
      return result;

    } else if (Coord.isMulti(s)) {
      Sexp sexp = new SList(new SSymbol("SUM"), new SSymbol(s));

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
      double d = 0;
      for (SSymbol cell : cells) {
        d += cell.accept(new SumVisitor(this.worksheet, this.cells, this.alreadyEval));
      }
      this.alreadyEval.put(sexp, new DoubleValue(d));
      return d;

    } else if (Coord.isMultiCols(s)) {
      Sexp sexp = new SList(new SSymbol("SUM"), new SSymbol(s));

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
      List<SSymbol> newCells = new ArrayList<>();
      for (Coord c : worksheet.getAllNonEmptyCells()) {
        if (colIndexes.contains(c.col)) {
          if (this.cells.contains(this.worksheet.getCellAt(c))) {
            throw new IllegalArgumentException("Cycle detected");
          } else {
            newCells.add(new SSymbol(c.toString()));
          }
        }
      }
      double d = 0;
      for (SSymbol cell : newCells) {
        d += cell.accept(new SumVisitor(this.worksheet, this.cells, this.alreadyEval));
      }
      this.alreadyEval.put(sexp, new DoubleValue(d));
      return d;

    } else if (Coord.isValidColName(s)) {
      Sexp sexp = new SList(new SSymbol("SUM"), new SSymbol(s));
      SValue sVal = this.alreadyEval.getOrDefault(sexp, null);
      if (sVal instanceof DoubleValue) {
        return ((DoubleValue) sVal).getValue();
      } else if (sVal != null) {
        throw new IllegalArgumentException();
      }
      List<SSymbol> newCells = new ArrayList<>();
      int colIndex = Coord.colNameToIndex(s);
      for (Coord c : worksheet.getAllNonEmptyCells()) {
        if (c.col == colIndex) {
          if (this.cells.contains(this.worksheet.getCellAt(c))) {
            throw new IllegalArgumentException("Cycle detected");
          } else {
            newCells.add(new SSymbol(c.toString()));
          }
        }
      }
      double d = 0;
      for (SSymbol cell : newCells) {
        d += cell.accept(new SumVisitor(this.worksheet, this.cells, this.alreadyEval));
      }
      this.alreadyEval.put(sexp, new DoubleValue(d));
      return d;

    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Double visitString(String s) {
    throw new IllegalArgumentException();
  }
}
