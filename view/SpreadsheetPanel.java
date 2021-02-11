package edu.cs3500.spreadsheets.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadableWorksheet;
import edu.cs3500.spreadsheets.model.SValue;

/**
 * Represents a panel containing a grid of cells that represent an area of a Worksheet.
 */
public class SpreadsheetPanel extends JPanel {

  private final ReadableWorksheet worksheet;
  private Coord topLeft;
  private final int cellHeight;
  private final int cellWidth;
  private Coord selected = null;
  private boolean isSelect = false;

  /**
   * Constructs a SpreadsheetPanel that represents an area of the given worksheet where the top
   * left corner is the given coordinate.
   * @param worksheet the worksheet to be shown
   * @param topLeft the top left coordinate of area of the spreadsheet that will be shown
   */
  SpreadsheetPanel(ReadableWorksheet worksheet, Coord topLeft, int cellWidth, int cellHeight) {
    this.worksheet = worksheet;
    setLayout(new BorderLayout());
    this.topLeft = topLeft;
    this.cellHeight = cellHeight;
    this.cellWidth = cellWidth;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    int numRow = this.getHeight() / this.cellHeight;
    int numCol = this.getWidth() / this.cellWidth;
    for (int row = 0; row < numRow; row++) {
      for (int col = 0; col < numCol; col++) {
        g2d.translate(col * cellWidth, row * cellHeight);
        this.drawCell(new Coord(col + topLeft.col, row + topLeft.row), g2d);
        g2d.translate(- col * cellWidth, - row * cellHeight);
      }
    }
  }

  private void drawCell(Coord c, Graphics2D g2d) {
    g2d.setColor(Color.WHITE);
    if (c.equals(this.selected) && this.isSelect) {
      g2d.setColor(Color.LIGHT_GRAY);
    }
    g2d.fillRect(0, 0, cellWidth, cellHeight);
    g2d.setColor(Color.BLACK);

    g2d.drawRect(0, 0, cellWidth, cellHeight);
    g2d.setFont(new Font("Sans Serif", Font.PLAIN, 16));
    SValue sVal = worksheet.getEvalFromCoord(c);
    g2d.setClip(0, 0, this.cellWidth, this.cellHeight);

    if (sVal != null) {
      g2d.drawString(sVal.toString(), 5,23);
    } else if (worksheet.getErrorAtCoord(c) != null) {
      g2d.setColor(Color.RED);
      g2d.drawString(worksheet.getErrorAtCoord(c), 5,23);
    }
    g2d.setClip(null);
  }

  void setTopLeft(Coord newC) {
    this.topLeft = newC;
  }

  /**
   * Selects the cell at the given coordinate.
   * @param selected the cell to be selected
   */
  void setSelected(Coord selected) {
    this.isSelect = true;
    this.selected = selected;
  }

  /**
   * Deselects the currently selected cell and updates the gui.
   */
  void deselect() {
    this.isSelect = false;
    this.repaint();
  }
}
