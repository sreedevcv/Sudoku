package sudoku;

import java.awt.BorderLayout;
// import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Sudoku {

    public Sudoku(int size) {

        JFrame frame = new JFrame("sudoku");
        SudokuPanel sudokuPanel = new SudokuPanel();
        ButtonPanel buttonPanel = new ButtonPanel();
        Board board = new Board(size);
        Cell.setBoadrSize(size);

        sudokuPanel.setBoard(board);
        buttonPanel.setBoard(board);
        buttonPanel.setSudokuPanel(sudokuPanel);
        
        // frame.setIconImage(new ImageIcon(Sudoku.class.getResource("logo.png").getPath()).getImage());
        // frame.setIconImage(new ImageIcon(new java.net.URL(null).getImage());

        // int x = Cell.rlim * sudokuPanel.cellSize + 3 * sudokuPanel.xOffset;
        // int y = Cell.clim * sudokuPanel.cellSize + 8 * sudokuPanel.yOffset;

        int x = 800;
        int y = 800;

        frame.setSize(x, y);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(sudokuPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        new Sudoku(16);
    }
}
