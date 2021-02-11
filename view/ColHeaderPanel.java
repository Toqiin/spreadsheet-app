package edu.cs3500.spreadsheets.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * Represents a panel containing the column headers in the visual representation of a Worksheet.
 */
public class ColHeaderPanel extends JPanel {

  private final int cellHeight;
  private final int cellWidth;
  private Coord topLeft;

  /**
   * Constructs a panel containing the column headers of a Worksheet.
   * @param topLeft the coordinate of the cell in the top left corner of the visible Worksheet
   */
  ColHeaderPanel(Coord topLeft, int cellWidth, int cellHeight) {
    this.topLeft = topLeft;
    this.cellHeight = cellHeight;
    this.cellWidth = cellWidth;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    int numCol = this.getWidth() / this.cellWidth;
    for (int col = 0; col < numCol; col++) {
      g2d.translate(col * this.cellWidth, 0);
      this.drawCell(col + this.topLeft.col, g2d);
      g2d.translate(-col * this.cellWidth, 0);
    }
  }

  private void drawCell(int col, Graphics2D g2d) {
    g2d.setColor(Color.lightGray);
    g2d.fillRect(0, 0, cellWidth, cellHeight);
    g2d.setColor(Color.BLACK);
    g2d.drawRect(0, 0, cellWidth, cellHeight);
    g2d.setFont(new Font("SansSerif", Font.BOLD, 16));

    String index = Coord.colIndexToName(col);
    g2d.setClip(0, 0, this.cellWidth, this.cellHeight);
    g2d.drawString(index, 5,23);
    g2d.setClip(null);
  }

  /**
   * Sets the top left coordinate to the given coordinate.
   * @param newC the new top left coord
   */
  void setTopLeft(Coord newC) {
    this.topLeft = newC;
  }
}
