package edu.cs3500.spreadsheets.model;

import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * Evaluates a Sexp into a SValue. If the Sexp is a formula, attempts to evaluate the formula to
 * get a SValue.
 */
public class EvalVisitor implements SexpVisitor<SValue> {
  final private ReadableWorksheet worksheet;
  final private List<Cell> cells;
  final private Map<Sexp, SValue> alreadyEval;

  /**
   * Constructs an EvalVisitor with a worksheet, worklist of cells, and map of already evaluated
   * Sexp.
   * @param worksheet   the worksheet
   * @param cells       the cells that have already been seen during evaluation
   * @param alreadyEval the Sexp that have already been evaluated during evaluation and its
   *                    evaluated SValue
   */
  public EvalVisitor(Worksheet worksheet, List<Cell> cells, Map<Sexp, SValue> alreadyEval) {
    this.worksheet = worksheet;
    this.cells = cells;
    this.alreadyEval = alreadyEval;
  }

  @Override
  public SValue visitBoolean(boolean b) {
    return new BooleanValue(b);
  }

  @Override
  public SValue visitNumber(double d) {
    return new DoubleValue(d);
  }

  @Override
  public SValue visitSList(List<Sexp> l) {
    if (l == null || l.size() <= 1) {
      throw new IllegalArgumentException("Not a valid function");
    }
    Sexp function = l.get(0);

    if (function.equals(new SSymbol("SUM"))) {
      double sum = 0;
      for (int i = 1; i < l.size(); i += 1 ) {
        sum += l.get(i).accept(new SumVisitor(this.worksheet, this.cells, this.alreadyEval));
      }
      return new DoubleValue(sum);
    } else if (function.equals(new SSymbol("PRODUCT"))) {
      double prod = 1;
      for (int i = 1; i < l.size(); i += 1) {
        prod *= l.get(i).accept(new ProdVisitor(this.worksheet, this.cells, this.alreadyEval));
      }
      return new DoubleValue(prod);
    } else if (function.equals(new SSymbol("<"))) {
      if (l.size() != 3) {
        throw new IllegalArgumentException("< takes strictly 2 arguments");
      }
      Double left = l.get(1).accept(new LessThanVisitor(this.worksheet, this.cells,
              this.alreadyEval));
      Double right = l.get(2).accept(new LessThanVisitor(this.worksheet, this.cells,
              this.alreadyEval));

      return new BooleanValue(left < right);
    } else if (function.equals(new SSymbol("CONCAT"))) {
      StringBuilder str = new StringBuilder();
      for (int i = 1; i < l.size(); i += 1) {
        str.append(l.get(i).accept(new ConcatVisitor(this.worksheet, this.cells,
                this.alreadyEval)));
      }
      return new StringValue(str.toString());
    } else {
      throw new IllegalArgumentException("Not a valid function");
    }
  }

  @Override
  public SValue visitSymbol(String s) {
    if (Coord.validCellName(s)) {
      return this.worksheet.evaluateCell(this.worksheet.getCell(s), this.cells, this.alreadyEval);
    } else if (Coord.isMulti(s)) {
      throw new IllegalArgumentException("Cell cannot be just a multi-cell reference.");
    } else {
      return new StringValue(s);
    }
  }

  @Override
  public SValue visitString(String s) {
    return new StringValue(s);
  }
}
