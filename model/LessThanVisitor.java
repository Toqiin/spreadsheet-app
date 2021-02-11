package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * Represents a visitor that attempts to evaluate different SExps to a Double so that it the SExp
 * can be compared to another SExp.
 */
public class LessThanVisitor implements SexpVisitor<Double> {

  final private ReadableWorksheet ws;
  final private List<Cell> cells;
  final private Map<Sexp, SValue> alreadyEval;

  /**
   * Constructs a LessThanVisitor with the given worksheet, worklist of cells, and maps of already
   * evaluated Sexps and their evaluated SValues.
   * @param worksheet   the worksheet
   * @param cells       the cells that have already been seen during evaluation
   * @param alreadyEval the SExps that have already been seen during evaluation and their
   *                    evaluated SValues
   */
  public LessThanVisitor(ReadableWorksheet worksheet, List<Cell> cells, Map<Sexp,
          SValue> alreadyEval) {
    this.ws = worksheet;
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
    if (curr != null) {
      throw new IllegalArgumentException();
    }

    if (l == null || l.size() <= 1) {
      throw new IllegalArgumentException();
    }
    Sexp function = l.get(0);
    if (function.equals(new SSymbol("SUM"))) {
      return new SumVisitor(this.ws, this.cells, this.alreadyEval).visitSList(l);
    } else if (function.equals(new SSymbol("PRODUCT"))) {
      return new ProdVisitor(this.ws, this.cells, this.alreadyEval).visitSList(l);
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Double visitSymbol(String s) {
    if (Coord.validCellName(s)) {
      Cell cell = this.ws.getCell(s);
      if (this.cells.contains(cell)) {
        throw new IllegalArgumentException();
      }
      if (cell == null) {
        throw new IllegalArgumentException("Cannot compare to an empty cell");
      }
      Sexp s1 = cell.convertString();
      List<Cell> copy = new ArrayList<>(this.cells);
      copy.add(cell);
      return s1.accept(new SumVisitor(this.ws, copy, this.alreadyEval));
    } else {
      throw new IllegalArgumentException("Not a cell");
    }
  }

  @Override
  public Double visitString(String s) {
    throw new IllegalArgumentException();
  }
}
