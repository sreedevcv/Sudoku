package com.sudoku;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ButtonPanel extends JPanel {

    private JButton resetButton = new JButton("Reset"); // Reset the board to initial state
    private JButton solveButton = new JButton("Solve"); // Tries to solve the puzzle from current state
    private JButton restoreButton = new JButton("Restore"); // Loads the last saved state
    private JButton nextButton = new JButton("Next"); // Moves to the next state by assigning a value to a cell
    private JButton genButton = new JButton("Generate"); // Generates a puzzle with a givem difficulty
    private JButton hintsButton = new JButton("Hints"); // Generates a puzzle with a givem difficulty
    private String[] optionsToChoose = { "Easy", "Medium", "Hard" };
    private JComboBox<String> jComboBox = new JComboBox<>(optionsToChoose);
    private JButton dropDownButton;
    private SudokuPanel sudokuPanel;
    private Board board;

    public void setSudokuPanel(SudokuPanel sudokuPanel) {
        this.sudokuPanel = sudokuPanel;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ButtonPanel() {

        jComboBox.setBounds(80, 50, 140, 20);
        this.setLayout(new FlowLayout());
        this.add(genButton);
        this.add(nextButton);
        this.add(solveButton);
        this.add(restoreButton);
        this.add(resetButton);
        this.add(hintsButton);
        this.add(jComboBox);

        addControls();
    }

    public void addControls() {

        resetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveState();
                board.clearBoard();
                sudokuPanel.paintImmediately(sudokuPanel.getBounds());
                sudokuPanel.setFocusable(true);
                sudokuPanel.requestFocusInWindow();
            }
        });

        nextButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                board.collapseBoard();
                board.updateBoard();
                sudokuPanel.paintImmediately(sudokuPanel.getBounds());

                int finished = board.finished();

                if (finished == -1)
                    JOptionPane.showMessageDialog(null, "Unsolvable!");
                else if (finished == 1)
                    JOptionPane.showMessageDialog(null, "Solved!");

                sudokuPanel.setFocusable(true);
                sudokuPanel.requestFocusInWindow();
            }
        });

        solveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                boolean isValid = true;
                boolean solvable = false;
                int finished = board.finished();

                if(finished >= 0) solvable = true;
                saveState();
                sudokuPanel.displayHints = true;

                while (solvable) {

                    isValid = true;

                    while (isValid) {
                        isValid = board.collapseBoard();

                        if (isValid) {
                            board.updateBoard();

                            try { Thread.sleep(20); }
                            catch(InterruptedException ie) {}
                            sudokuPanel.paintImmediately(sudokuPanel.getBounds());
                        }
                    }

                    finished = board.finished();
                    if (finished == 1)
                        break;

                    loadState();
                }

                sudokuPanel.paintImmediately(sudokuPanel.getBounds());
                if (finished == 1)
                    JOptionPane.showMessageDialog(null, "Solved!");
                else if (finished == -1)
                    JOptionPane.showMessageDialog(null, "Unsolvable!");

                sudokuPanel.setFocusable(true);
                sudokuPanel.requestFocusInWindow();
                sudokuPanel.displayHints = false;

            }
        });

        restoreButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loadState();
                board.updateBoard();
                sudokuPanel.paintImmediately(sudokuPanel.getBounds());
                sudokuPanel.setFocusable(true);
                sudokuPanel.requestFocusInWindow();
            }
        });

        genButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveState();
                board.generatePuzzle();
                sudokuPanel.paintImmediately(sudokuPanel.getBounds());
                sudokuPanel.setFocusable(true);
                sudokuPanel.requestFocusInWindow();
            }
        });

        hintsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!sudokuPanel.displayHints)
                    sudokuPanel.displayHints = true;
                else
                    sudokuPanel.displayHints = false;

                sudokuPanel.paintImmediately(sudokuPanel.getBounds());
                sudokuPanel.setFocusable(true);
                sudokuPanel.requestFocusInWindow();
            }
        });

        jComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                String state = e.getItem().toString();
                board.difficulty = state;
            }
        });
    }

    // Saves the state of the Board.grid onto savedBooard.txt
    public static void saveState() {

        try {
            FileOutputStream fout = new FileOutputStream("./target/classes/com/sudoku/savedBoard.txt");
            ObjectOutputStream oStream = new ObjectOutputStream(fout);

            oStream.writeObject(Board.grid);
            oStream.flush();
            fout.close();
            oStream.close();

        } catch (IOException ioe) {
            System.out.println("Exception during serialization.");
            ioe.printStackTrace();
        }
    }

    // Loads the saved state of Board.grid object from savedBoard
    public static void loadState() {

        try {
            FileInputStream fin = new FileInputStream("./target/classes/com/sudoku/savedBoard.txt");
            ObjectInputStream inStream = new ObjectInputStream(fin);

            Board.grid = (ArrayList<ArrayList<Cell>>) inStream.readObject();
            inStream.close();
            fin.close();

        } catch (IOException ioe) {
            System.out.println("Exception during deserialization");
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Exception during deserialization");
            cnfe.printStackTrace();
        }
    }

}
