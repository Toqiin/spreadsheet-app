package edu.cs3500.spreadsheets.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

import javax.swing.JComponent;

import edu.cs3500.spreadsheets.controller.SpreadsheetFeatures;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadableWorksheet;

/**
 * Represents a textual edu.cs3500.spreadsheets.view for a Worksheet that saves the Worksheet as a file.
 */
public class SavingFilesView implements SpreadsheetView {

  private ReadableWorksheet worksheet;
  private Appendable app;

  /**
   * Constructs a SavingFilesView.
   * @param worksheet the worksheet to be saved
   * @param app the appendable that the worksheet will be appended to
   */
  public SavingFilesView(ReadableWorksheet worksheet, Appendable app) {
    this.worksheet = worksheet;
    this.app = app;
  }

  /**
   * Constructs a SavingFilesView.
   * @param worksheet the worksheet to be saved
   */
  public SavingFilesView(ReadableWorksheet worksheet) {
    this.worksheet = worksheet;
  }

  @Override
  public void render() {
    this.worksheet.evalAllCells();
    Set<Coord> nonEmptyCoord = worksheet.getAllNonEmptyCells();
    try {
      for (Coord c : nonEmptyCoord) {
        this.app = this.app.append(c.toString()).append(" ").append(this.worksheet.getCellAt(c)
                .getString()).append("\n");
      }
    } catch (IOException e) {
      System.out.println("Could not append");
      System.exit(0);
    }
  }

  @Override
  public ReadableWorksheet getWorksheet() {
    return this.worksheet;
  }

  @Override
  public void addFeatures(SpreadsheetFeatures features) {
    // does not currently interact with a edu.cs3500.spreadsheets.controller
  }

  @Override
  public JComponent getMain() {
    throw new UnsupportedOperationException("Cannot get the main panel from a textual edu.cs3500.spreadsheets.view.");
  }

  @Override
  public void save(File dest) throws FileNotFoundException {
    if (dest.exists()) {
      System.out.println("File already exists. Cannot override.");
      System.exit(0);
    } else {
      PrintWriter pw = new PrintWriter(dest);
      Set<Coord> nonEmptyCoord = worksheet.getAllNonEmptyCells();
      for (Coord c : nonEmptyCoord) {
        pw.print(c.toString() + " " + this.worksheet.getCellAt(c).getString() + "\n");
      }
      pw.close();
    }
  }

  @Override
  public void showCell(Coord c) {
    // all cells are already shown because it's a file
  }

  @Override
  public String toString() {
    String result = "";
    Set<Coord> cells = this.worksheet.getAllNonEmptyCells();
    ArrayList<String> str = new ArrayList<String>();
    for (Coord c : cells) {
      str.add(c.toString() + " " + this.worksheet.getCellAt(c).getString());
    }
    str.sort( Comparator.comparing( String::toString ));
    for (String s : str) {
      result = result + s + "\n";
    }
    return result;
  }

}
