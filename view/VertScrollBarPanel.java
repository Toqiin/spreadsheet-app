package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * Represents a panel containing the vertical scroll bar in the visual representation of a
 * Worksheet that contains buttons that shifts the Worksheet up or down.
 */
public class VertScrollBarPanel extends JPanel {

  private JButton scrollUp;
  private JButton scrollDown;

  /**
   * Constructs a VertScrollBarPanel.
   */
  VertScrollBarPanel() {
    this.setPreferredSize(new Dimension(30, 480));
    this.scrollUp = new JButton("↑");
    this.scrollDown = new JButton("↓");
    this.scrollUp.setBounds(0, 0, 30, 30);
    this.scrollDown.setBounds(0, 450, 30, 30);
    this.scrollUp.setFont(new Font("Serif", Font.BOLD, 16));
    this.scrollDown.setFont(new Font("Serif", Font.BOLD, 16));
    JPanel filler1 = new JPanel();
    filler1.setPreferredSize(new Dimension(30, 185));
    this.add(filler1);
    this.add(this.scrollUp);
    JPanel filler2 = new JPanel();
    filler2.setPreferredSize(new Dimension(30, 10));
    this.add(filler2);
    this.add(this.scrollDown);
    JPanel filler3 = new JPanel();
    filler3.setPreferredSize(new Dimension(30, 225));
    this.add(filler3);
  }

  JButton getScrollUp() {
    return this.scrollUp;
  }

  JButton getScrollDown() {
    return this.scrollDown;
  }

}
