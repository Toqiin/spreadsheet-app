package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JTextField;

import edu.cs3500.spreadsheets.controller.SpreadsheetFeatures;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadableWorksheet;

/**
 * Represents a edu.cs3500.spreadsheets.view of the Spreadsheet that the user can interact with. The user can select cells,
 * edit the content of cells, and scroll indefinitely to the right and down to access cells.
 */
public class InteractiveView extends JFrame implements SpreadsheetView {

  private ReadableWorksheet worksheet;
  private MainPanel main;
  private InputPanel input;
  private Coord selected = null;
  private boolean isSelect = false;

  /**
   * Constructs a Interactive edu.cs3500.spreadsheets.view.
   * @param ws the edu.cs3500.spreadsheets.view that this edu.cs3500.spreadsheets.view will build upon by adding functionality
   */
  public InteractiveView(ReadableWorksheet ws) {
    super();
    this.setTitle("Spreadsheet!");
    this.setSize(900, 680);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLayout(new FlowLayout());
    this.worksheet = ws;
    this.worksheet.evalAllCells();

    this.input = new InputPanel(this.worksheet);
    this.input.setPreferredSize(new Dimension(885, 60));
    this.add(this.input);

    this.main = new MainPanel(worksheet);
    this.main.setPreferredSize(new Dimension(900, 570));
    this.add(main);

    this.main.getSS().addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        mouseClickedInSpreadSheet(e);
      }
    });

    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_S)
                && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) {
          System.out.println("Saving...");
        }
      }
    });

  }

  @Override
  public void save(File dest) throws FileNotFoundException {
    // cannot be saved
  }

  @Override
  public void showCell(Coord cell) {
    // all cells are shown
  }

  @Override
  public void render() {
    this.setVisible(true);
    this.repaint();
  }

  @Override
  public ReadableWorksheet getWorksheet() {
    return this.worksheet;
  }

  @Override
  public void addFeatures(SpreadsheetFeatures features) {
    JTextField text = this.input.getTextField();
    this.input.getCheckButton().addActionListener(evt -> features.setString(
            this.selected, text.getText()));
  }

  public MainPanel getMain() {
    return this.main;
  }

  private void mouseClickedInSpreadSheet(MouseEvent e) {
    Coord sel = this.main.getCoordFromME(e);
    if (sel.equals(this.selected) && this.isSelect) {
      this.deselect();
    } else {
      this.isSelect = true;
      this.selected = sel;
      this.main.setSelected(sel);

      this.input.setSelected(sel);
      this.repaint();
    }

  }

  private void deselect() {
    this.isSelect = false;
    this.input.deselect();
    this.main.deselect();
  }

}
