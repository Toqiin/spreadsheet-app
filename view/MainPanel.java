package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadableWorksheet;

/**
 * Represents the main panel in the SpreadsheetFrame that contains the edu.cs3500.spreadsheets.view of the Spreadsheet and
 * the scroll bars. Allows the user to scroll indefinitely to the right and down and select cells in
 * the worksheet.
 */
public class MainPanel extends JPanel {

  private final ReadableWorksheet worksheet;
  private final SpreadsheetPanel spreadsheetPanel;
  private final ColHeaderPanel header;
  private final RowHeaderPanel rowHeader;
  private Coord topLeftCoord;
  private final int cellHeight = 30;
  private final int cellWidth = 80;

  /**
   * Constructs a SpreadsheetFrame that renders the given Worksheet.
   * @param worksheet the worksheet to be rendered
   */
  public MainPanel(ReadableWorksheet worksheet) {
    super();
    this.worksheet = worksheet;
    this.topLeftCoord = new Coord(1, 1);
    this.setSize(890, 570);
    this.setLayout(new FlowLayout());

    JPanel topLeft = new JPanel();
    topLeft.setPreferredSize(new Dimension(30, 30));
    this.add(topLeft);

    this.header = new ColHeaderPanel(this.topLeftCoord, this.cellWidth, this.cellHeight);
    header.setPreferredSize(new Dimension(800, 30));
    this.add(header);

    JPanel topRight = new JPanel();
    topRight.setPreferredSize(new Dimension(30, 30));
    this.add(topRight);

    this.rowHeader = new RowHeaderPanel(this.topLeftCoord, this.cellWidth, this.cellHeight);
    rowHeader.setPreferredSize(new Dimension(30, 480));
    this.add(rowHeader);

    this.spreadsheetPanel = new SpreadsheetPanel(worksheet,
            this.topLeftCoord, this.cellWidth, this.cellHeight);
    spreadsheetPanel.setPreferredSize(new Dimension(800, 480));
    this.add(spreadsheetPanel);

    VertScrollBarPanel vertScrollBar = new VertScrollBarPanel();
    vertScrollBar.setPreferredSize(new Dimension(30, 480));
    this.add(vertScrollBar);

    JPanel bottomLeft = new JPanel();
    bottomLeft.setPreferredSize(new Dimension(30, 40));
    this.add(bottomLeft);

    HorizontalScrollBarPanel horizScrollBar = new HorizontalScrollBarPanel();
    horizScrollBar.setPreferredSize(new Dimension(800, 40));
    this.add(horizScrollBar);

    JPanel bottomRight = new JPanel();
    bottomRight.setPreferredSize(new Dimension(30, 40));
    this.add(bottomRight);

    vertScrollBar.getScrollUp().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        changeScroll(0, -1);
      }
    });

    vertScrollBar.getScrollDown().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        changeScroll(0, 1);
      }
    });

    horizScrollBar.getScrollLeft().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        changeScroll(-1, 0);
      }
    });

    horizScrollBar.getScrollRight().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        changeScroll(1, 0);
      }
    });
  }

  private void changeScroll(int x, int y) {
    try {
      this.topLeftCoord = new Coord(this.topLeftCoord.col + x, this.topLeftCoord.row + y);
    } catch (IllegalArgumentException e) {
      // does nothing
    }
    this.spreadsheetPanel.setTopLeft(this.topLeftCoord);
    this.spreadsheetPanel.repaint();
    this.header.setTopLeft(this.topLeftCoord);
    this.header.repaint();
    this.rowHeader.setTopLeft(this.topLeftCoord);
    this.rowHeader.repaint();
  }

  /**
   * Selects the cell at the given coordinate. Delegates to the spreadsheetpanel.
   * @param selected the cell to be selected
   */
  void setSelected(Coord selected) {
    this.spreadsheetPanel.setSelected(selected);
  }

  JPanel getSS() {
    return this.spreadsheetPanel;
  }

  /**
   * Converts the given mouse event to the coordinate of the cell that was clicked on.
   * @param e the spot that was clicked
   * @return the coordinate of the clicked cell
   */
  Coord getCoordFromME(MouseEvent e) {
    return new Coord((e.getX() / this.cellWidth) + this.topLeftCoord.col,
            (e.getY() / this.cellHeight) + this.topLeftCoord.row);
  }

  /**
   * Deselects the cell that is currently selected. Delegates to the spreadsheetpanel.
   */
  void deselect() {
    this.spreadsheetPanel.deselect();
  }
}
