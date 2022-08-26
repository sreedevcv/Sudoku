/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sudoku;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author sreedev
 */
public class Sudoku {

    /**
     * @param args the command line arguments
     */

    JFrame frame;
    SudokuPanel sudokuPanel;
    ButtonPanel buttonPanel;
    Board board;

    public Sudoku() {

        frame = new JFrame("sudoku");
        board = new Board();
        sudokuPanel = new SudokuPanel();

        Cell.board = board;
        SudokuPanel.board = board;

        buttonPanel = new ButtonPanel();
        buttonPanel.board = board;
        buttonPanel.sudokuPanel = sudokuPanel;
        frame.setIconImage(new ImageIcon(Sudoku.class.getResource("logo.png").getPath()).getImage());

        int x = Cell.rlim * sudokuPanel.cellSize + 3 * sudokuPanel.xOffset;
        int y = Cell.clim * sudokuPanel.cellSize + 8 * sudokuPanel.yOffset;

        frame.setSize(x, y);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(sudokuPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // SudokuPanel.generatePuzzle();
    }

    public static void main(String[] args) {

        new Sudoku();
    }
}

/*
Store the main files in ~/prg/snake/main/java/com/sudoku
From ~/prg/snake/main/java/com/sudoku run    javac -d ~/prg/snake/target/classes/ *.java
From ~/prg/snake/ run     java -cp ~/prg/snake/target/classes/ com.sudoku.Sudoku
Make sure that logo.png is in ~/prg/snake/target/classes/com/sudoku


sreedev➤ ~/prg/snake/Sudoku ❯❯❯ javac -d target/classes/ ./main/java/com/sudoku/*.java   
sreedev➤ ~/prg/snake/Sudoku ❯❯❯ mv main/java/com/sudoku/logo.png target/classes/com/sudoku/
sreedev➤ ~/prg/snake/Sudoku ❯❯❯ java -cp target/classes/ com.sudoku.Sudoku     
 */