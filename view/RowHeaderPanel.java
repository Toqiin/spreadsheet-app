package edu.cs3500.spreadsheets.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * Represents a panel containing the row headers in the visual representation of a Worksheet.
 */
public class RowHeaderPanel extends JPanel {

  private final int cellHeight;
  private final int cellWidth;
  private Coord topLeft;

  /**
   * Constructs a panel containing the row headers of a Worksheet.
   * @param topLeft the coordinate of the cell in the top left corner of the visible Worksheet
   */
  public RowHeaderPanel(Coord topLeft, int cellWidth, int cellHeight) {
    this.topLeft = topLeft;
    this.cellHeight = cellHeight;
    this.cellWidth = cellWidth;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    int numRow = this.getHeight() / this.cellHeight;
    for (int row = 0; row < numRow; row++) {
      g2d.translate(0, row * this.cellHeight);
      this.drawCell(row + topLeft.row, g2d);
      g2d.translate(0, -row * this.cellHeight);

    }
  }

  private void drawCell(int row, Graphics2D g2d) {
    g2d.setColor(Color.lightGray);
    g2d.fillRect(0, 0, 30, cellHeight);
    g2d.setColor(Color.BLACK);
    g2d.drawRect(0, 0, 30, cellHeight);
    g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
    String index = Integer.toString(row);
    g2d.setClip(0, 0, 30, this.cellHeight);
    g2d.drawString(index, 5,23);
    g2d.setClip(null);
  }

  void setTopLeft(Coord newC) {
    this.topLeft = newC;
  }
}
