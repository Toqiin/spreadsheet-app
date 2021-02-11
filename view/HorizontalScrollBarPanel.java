package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * Represents a panel containing the horizontal scroll bar in the visual representation of a
 * Worksheet that contains buttons that shifts the Worksheet right or left.
 */
public class HorizontalScrollBarPanel extends JPanel {

  private JButton scrollLeft;
  private JButton scrollRight;

  /**
   * Contructs a HorizontalScrollBarPanel.
   */
  HorizontalScrollBarPanel() {
    this.setPreferredSize(new Dimension(800, 40));
    this.scrollLeft = new JButton("←");
    this.scrollRight = new JButton("→");
    this.scrollLeft.setBounds(0, 0, 30, 30);
    this.scrollRight.setBounds(450, 0, 30, 30);
    this.scrollLeft.setFont(new Font("Serif", Font.BOLD, 16));
    this.scrollRight.setFont(new Font("Serif", Font.BOLD, 16));
    JPanel filler1 = new JPanel();
    filler1.setPreferredSize(new Dimension(235, 30));
    this.add(filler1);
    this.add(this.scrollLeft);
    JPanel filler2 = new JPanel();
    filler2.setPreferredSize(new Dimension(10, 30));
    this.add(filler2);
    this.add(this.scrollRight);
    JPanel filler3 = new JPanel();
    filler3.setPreferredSize(new Dimension(235, 30));
    this.add(filler3);
  }

  JButton getScrollLeft() {
    return this.scrollLeft;
  }

  JButton getScrollRight() {
    return this.scrollRight;
  }
}
