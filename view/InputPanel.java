package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadableWorksheet;

/**
 * Represents the panel at the top of the Interactive edu.cs3500.spreadsheets.view that enables users to edit the content
 * of cells.
 */
public class InputPanel extends JPanel {

  private JButton accept;
  private JButton reject;
  private JTextField formula;
  private Coord selected;
  private ReadableWorksheet ws;
  private boolean isSelect = false;

  /**
   * Constructs a InputPanel.
   * @param ws the worksheet whose cells the input panel can display
   */
  public InputPanel(ReadableWorksheet ws) {
    super();
    this.ws = ws;
    this.setSize(885, 60);
    this.setLayout(new FlowLayout());

    this.accept = new JButton("✓");
    this.accept.setPreferredSize(new Dimension(60, 50));
    this.accept.setFont(new Font("Sans Serif", Font.PLAIN, 32));
    this.add(this.accept);

    this.reject = new JButton("X");
    this.reject.setPreferredSize(new Dimension(60, 50));
    this.reject.setFont(new Font("Sans Serif", Font.PLAIN, 32));
    this.add(this.reject);

    this.formula = new JTextField();
    this.formula.setPreferredSize(new Dimension(675, 50));
    this.formula.setFont(new Font("Sans Serif", Font.PLAIN, 32));
    this.formula.setMargin(new Insets(0, 10, 0, 0));
    this.add(this.formula);

    this.reject.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (isSelect) {
          formula.setText(ws.getCellAt(selected).getString());
        } else {
          if (ws.getCellAt(selected) == null) {
            formula.setText("");
          }
        }
        formula.repaint();
      }
    });
  }

  JButton getCheckButton() {
    return this.accept;
  }

  JTextField getTextField() {
    return this.formula;
  }

  /**
   * Selects the cell at the given coordinate and displays the contents in the text field.
   * @param sel the cell to be selected
   */
  void setSelected(Coord sel) {
    this.isSelect = true;
    this.selected = sel;
    if (ws.getCellAt(selected) == null) {
      formula.setText("");
    } else {
      formula.setText(ws.getCellAt(selected).getString());
    }
  }

  /**
   * Deselects the currently selected cell and clears the text field.
   */
  void deselect() {
    this.isSelect = false;
    formula.setText("");
  }
}
