package edu.cs3500.spreadsheets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.cs3500.spreadsheets.controller.SpreadsheetController;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.SValue;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.view.InteractiveView;
import edu.cs3500.spreadsheets.view.SavingFilesView;
import edu.cs3500.spreadsheets.view.SpreadsheetFrame;
import edu.cs3500.spreadsheets.view.SpreadsheetView;

/**
 * The main class for our program.
 */
public class BeyondGood {
  /**
   * The main entry point.
   * @param args any command-line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    switch (args[0]) {
      case "-in":
        FileReader fr = getFile(args);
        Worksheet ws = getWorksheet(fr);
        if (args.length == 3 && args[2].equals("-gui")) {
          SpreadsheetView view = new SpreadsheetFrame(ws);
          view.render();
        } else if (args.length == 3 && args[2].equals("-edit")) {
          SpreadsheetView view2 = new InteractiveView(ws);
          SpreadsheetController cont = new SpreadsheetController(ws);
          cont.setView(view2);
          view2.render();
        } else if (args.length == 4) {
          if (args[2].equals("-eval")) {
            ensureCell(args[3]);
            Map<Sexp, SValue> alreadyEval = new HashMap<>();
            evalAll(ws, args, alreadyEval);
            evalCell(ws, args, alreadyEval);
          } else if (args[2].equals("-save")) {
            try {
              SpreadsheetView view = new SavingFilesView(ws);
              view.save(new File(args[3]));
            } catch (FileNotFoundException e) {
              System.out.println("Error. Could not save to " + args[3]);
              System.exit(0);
            }
          }
        }
        break;
      case "-gui":
        if (args.length != 1) {
          System.out.println("Error. Takes no arguments");
          System.exit(0);
        }
        ws = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
        SpreadsheetView view = new SpreadsheetFrame(ws);
        view.render();
        break;
      case "-edit":
        ws = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
        SpreadsheetView view2 = new InteractiveView(ws);
        SpreadsheetController cont = new SpreadsheetController(ws);
        cont.setView(view2);
        view2.render();
        break;
      default:
        System.out.println("Error. Invalid command.");
        System.exit(0);
    }
  }

  private static FileReader getFile(String[] args) {
    try {
      File file = new File(args[1]);
      return new FileReader(file);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found.");
    }
  }

  private static Worksheet getWorksheet(FileReader fr) {
    WorksheetReader.WorksheetBuilder<BasicWorksheet> b = new BasicWorksheet.BasicWorksheetBuilder();
    Worksheet ws = WorksheetReader.read(b, fr);
    return ws;
  }

  private static void ensureCell(String s) {
    if (!Coord.validCellName(s)) {
      System.out.println("Error. 4th argument must be a valid cell.");
      System.exit(0);
    }
  }

  private static void evalAll(Worksheet ws, String[] args, Map<Sexp, SValue> alreadyEval) {
    if (!args[2].equals("-eval")) {
      System.out.println("Error. Third argument must be '-eval'.");
      System.exit(0);
    }
    boolean allPass = true;
    Set<Coord> cells = ws.getAllNonEmptyCells();

    for (Coord cell : cells) {
      try {
        SValue ignored = ws.evaluateCell(ws.getCellAt(cell), alreadyEval);
        alreadyEval.put(ws.getCellAt(cell).convertString(), ignored);
      } catch (IllegalArgumentException e) {
        allPass = false;
        System.out.println("Error in cell." + " " + cell.toString() + ". " + e.getMessage());
      }
    }

    if (!allPass) {
      System.out.println("Error. All cells must be able to evaluate to an atomic value.");
      System.exit(0);
    }
  }

  private static void evalCell(Worksheet ws, String[] args, Map<Sexp, SValue> alreadyEval) {
    SValue val = ws.evaluateCell(ws.getCell(args[3]), alreadyEval);
    System.out.print(val.toString());
  }
}
