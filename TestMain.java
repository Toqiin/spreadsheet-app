package edu.cs3500.spreadsheets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.cs3500.spreadsheets.controller.SpreadsheetController;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.InteractiveView;
import edu.cs3500.spreadsheets.view.SpreadsheetView;

/**
 * This is a class for running our program in an easily changeable way from
 * within the code.
 */
public class TestMain {

  /**
   * Runs the program.
   * @param args string inputs to main method.
   * @throws FileNotFoundException if the filereader cant read the file.
   */
  public static void main(String[] args) throws FileNotFoundException {
    WorksheetReader.WorksheetBuilder<BasicWorksheet> b2 =
            new BasicWorksheet.BasicWorksheetBuilder();
    FileReader fr2 = new FileReader(new File("resources/triangle.txt"));
    Worksheet ws2 = WorksheetReader.read(b2, fr2);
    SpreadsheetView view = new InteractiveView(ws2);
    SpreadsheetController cont = new SpreadsheetController(ws2);
    cont.setView(view);
    view.render();
  }
}
