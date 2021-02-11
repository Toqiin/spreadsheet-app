package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.cs3500.spreadsheets.sexp.SSymbol;

/**
 * A value type representing coordinates in a {@link Worksheet}.
 */
public class Coord {
  public final int row;
  public final int col;

  /**
   * Constructs a coord with the given row and column of a worksheet.
   * @param col the column in a worksheet
   * @param row the row in the worksheet
   */
  public Coord(int col, int row) {
    if (row < 1 || col < 1) {
      throw new IllegalArgumentException("Coordinates should be strictly positive");
    }
    this.row = row;
    this.col = col;
  }

  /**
   * Converts from the A-Z column naming system to a 1-indexed numeric value.
   * @param name the column name
   * @return the corresponding column index
   */
  static int colNameToIndex(String name) {
    name = name.toUpperCase();
    int ans = 0;
    for (int i = 0; i < name.length(); i++) {
      ans *= 26;
      ans += (name.charAt(i) - 'A' + 1);
    }
    return ans;
  }

  /**
   * Converts a 1-based column index into the A-Z column naming system.
   * @param index the column index
   * @return the corresponding column name
   */
  public static String colIndexToName(int index) {
    StringBuilder ans = new StringBuilder();
    while (index > 0) {
      int colNum = (index - 1) % 26;
      ans.insert(0, Character.toChars('A' + colNum));
      index = (index - colNum) / 26;
    }
    return ans.toString();
  }

  @Override
  public String toString() {
    return colIndexToName(this.col) + this.row;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coord coord = (Coord) o;
    return row == coord.row
        && col == coord.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

  /**
   * Converts the given cell name to the corresponding Coord in a Worksheet.
   * Cell names contain the column which
   *    * follows the pattern:
   *    * Columns 1 through 26 get names A through Z
   *    * Columns 27 through 52 get names AA through AZ
   *    * ...
   *    * and the row which starts counting from 1.
   * @param s the name of the cell
   * @return the coordinate of the cell in a Worksheet
   */
  public static Coord coordFromString(String s) {
    if (!validCellName(s)) {
      throw new IllegalArgumentException("String is not a valid Coord.");
    }
    int i = 0;
    int firstNum = -1;
    while (i < s.length()) {
      if (Character.isDigit(s.charAt(i))) {
        firstNum = i;
        break;
      } else {
        i += 1;
      }
    }
    String col = s.substring(0, firstNum);
    int colInd = Coord.colNameToIndex(col);
    int rowInd = Integer.parseInt(s.substring(firstNum));
    return new Coord(colInd, rowInd);
  }

  /**
   * Determines if the given string represents a range of cells in a Worksheet. A range of cells
   * has the format of CN:CN, where CN is a valid cell name.
   * @param s the string to be checked
   * @return true if the given string represents a range of cells in a Worksheet
   */
  public static boolean isMulti(String s) {
    if (s.contains(":")) {
      int ind = s.indexOf(":");
      if (ind >= s.length()) {
        return false;
      }
      String left = s.substring(0, ind);
      String right = s.substring(ind + 1);
      return validCellName(left) && validCellName(right);
    } else {
      return false;
    }
  }

  /**
   * Determines if the given string is the name of a cell in a Worksheet. Cell names follow the
   * pattern SLN where SL is a sequence of letters and N is a positive integer.
   * @param s the string to be cheecked
   * @return true if the given string is the name of a cell in a Worksheet
   */
  public static boolean validCellName(String s) {
    int i = 0;
    int firstNum = -1;
    while (i < s.length()) {
      if (Character.isDigit(s.charAt(i))) {
        firstNum = i;
        break;
      } else {
        i += 1;
      }
    }
    if (firstNum <= 0) {
      return false;
    } else {
      String col = s.substring(0, firstNum);
      boolean result = true;
      for (char c : col.toCharArray()) {
        result = result && Character.isLetter(c);
      }
      String row = s.substring(firstNum);
      for (char c : row.toCharArray()) {
        result = result && Character.isDigit(c);
      }
      return result;
    }
  }

  /**
   * Generates a list of the name of each cell that is within the range specified by the given
   * strings in the Worksheet. Assumes that each string is a valid cell name or column name.
   *
   * @param left  one side of the range
   * @param right other side of the range
   * @return the names of all the cells that are within the range
   */
  public static List<SSymbol> getCellsBetween(String left, String right) {
    int i = 0;
    int firstNum = -1;
    while (i < left.length()) {
      if (Character.isDigit(left.charAt(i))) {
        firstNum = i;
        break;
      } else {
        i += 1;
      }
    }
    int leftCol = colNameToIndex(left.substring(0, firstNum));
    int leftRow = Integer.parseInt(left.substring(firstNum));

    i = 0;
    firstNum = -1;
    while (i < right.length()) {
      if (Character.isDigit(right.charAt(i))) {
        firstNum = i;
        break;
      } else {
        i += 1;
      }
    }
    int rightCol = colNameToIndex(right.substring(0, firstNum));
    int rightRow = Integer.parseInt(right.substring(firstNum));

    int lowRow;
    int highRow;

    if (leftRow < rightRow) {
      lowRow = leftRow;
      highRow = rightRow;
    } else {
      lowRow = rightRow;
      highRow = leftRow;
    }

    List<Integer> rows = new ArrayList<>();
    for (int idx = lowRow; idx <= highRow; idx += 1) {
      rows.add(idx);
    }

    int lowCol;
    int highCol;

    if (leftCol < rightCol) {
      lowCol = leftCol;
      highCol = rightCol;
    } else {
      lowCol = rightCol;
      highCol = leftCol;
    }


    List<Integer> cols = new ArrayList<>();
    for (int idx = lowCol; idx <= highCol; idx += 1) {
      cols.add(idx);
    }

    List<SSymbol> result = new ArrayList<>();
    for (int col : cols) {
      for (int row : rows) {
        String cellName = colIndexToName(col) + Integer.toString(row);
        result.add(new SSymbol(cellName));
      }
    }
    return result;
  }

  /**
   * Determines if the given string is a valid column name in the spreadsheet.
   * @param s the string to be checked
   * @return if the string is only composed of letters
   */
  static boolean isValidColName(String s) {
    char[] row = s.toCharArray();
    for (char c : row) {
      if (!Character.isLetter(c)) {
        return false;
      }
    }
    return true;
  }

  /**
   *
   * @param s
   * @return
   */
  static boolean isMultiCols(String s) {
    if (s.contains(":")) {
      int ind = s.indexOf(":");
      if (ind >= s.length()) {
        return false;
      }
      String left = s.substring(0, ind);
      String right = s.substring(ind + 1);
      return isValidColName(left) && isValidColName(right);
    } else {
      return false;
    }
  }

  static List<Integer> getColsBetween(String left, String right) {
    int leftIdx = Coord.colNameToIndex(left);
    int rightIdx = Coord.colNameToIndex(right);
    int big;
    int small;
    if (leftIdx > rightIdx) {
      big = leftIdx;
      small = rightIdx;
    } else {
      big = rightIdx;
      small = leftIdx;
    }
    List<Integer> cols = new ArrayList<>();
    for (int i = small; i <= big; i ++) {
      cols.add(i);
    }
    return cols;
  }

}
