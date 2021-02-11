package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JFrame;

import edu.cs3500.spreadsheets.controller.SpreadsheetFeatures;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadableWorksheet;
import edu.cs3500.spreadsheets.model.Worksheet;

/**
 * Represents a gui visual representation of a Worksheet, drawn as a grid of cells containing
 * their evaluated values which can be scrolled (horizontally or vertically) to reveal more cells.
 */
public class SpreadsheetFrame extends JFrame implements SpreadsheetView {

  private final MainPanel panel;
  private final Worksheet worksheet;

  /**
   * Constructs a SpreadsheetFrame that renders the given Worksheet.
   * @param worksheet the worksheet to be rendered
   */
  public SpreadsheetFrame(Worksheet worksheet) {
    super();
    this.worksheet = worksheet;
    this.worksheet.evalAllCells();
    this.panel = new MainPanel(worksheet);
    this.setTitle("Spreadsheet!");
    this.setSize(890, 600);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLayout(new FlowLayout());

    this.panel.setPreferredSize(new Dimension(890, 600));
    this.add(this.panel);
  }


  @Override
  public void save(File dest) throws FileNotFoundException {
    // cannot currently be saved
  }

  @Override
  public void showCell(Coord cell) {
    // all cells are being shown
  }

  @Override
  public void render() {
    this.setVisible(true);
  }

  @Override
  public ReadableWorksheet getWorksheet() {
    return this.worksheet;
  }

  @Override
  public void addFeatures(SpreadsheetFeatures features) {
    // this edu.cs3500.spreadsheets.view has no features
  }

  @Override
  public JComponent getMain() {
    return this.panel;
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
