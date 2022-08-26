package com.sudoku;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Font;
import javax.swing.JPanel;

public class SudokuPanel extends JPanel {

    int cellSize = 80;
    int row = Cell.rlim;
    int col = Cell.clim;
    int xOffset = 10;
    int yOffset = 10;
    Cell highlightCell = new Cell(-1, -1);
    boolean displayHints = false;
    static Board board;

    SudokuPanel() {
        addControls();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        this.removeAll();
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                int x = i * cellSize + xOffset;
                int y = j * cellSize + yOffset;
                Cell cell = Board.getCell(i, j);

                if (cell == highlightCell) // Draws the highlighted cell
                    g.setColor(Color.LIGHT_GRAY);
                else
                    g.setColor(Color.BLACK);

                g.fillRect(x, y, cellSize, cellSize);

                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize); // Draws the small cell
                g.setColor(Color.RED);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
                if (cell.value != 0) // Writes the value of the cell
                    g.drawString(String.valueOf(cell.value), (int) (x + cellSize / 2), (int) (y + cellSize / 2));

                g.setFont(new Font("Serif", Font.PLAIN, 10));
                if (displayHints)
                    displayPossibleValues(cell, g, x, y); // Displays the possible values of a cell
            }
        }

        g.setColor(Color.WHITE);

        // Divides the grid into 81 squares
        for (int i = 1; i < 9; i++) {
            g.drawLine(i * cellSize + xOffset, yOffset,
                    i * cellSize + xOffset, yOffset + 9 * cellSize);

            g.drawLine(xOffset, i * cellSize + yOffset,
                    xOffset + 9 * cellSize, i * cellSize + yOffset);
        }

        g.setColor(Color.green);

        // Divides the grid into 9 subsquares
        for (int i = 1; i < 3; i++) {
            g.drawLine(i * 3 * cellSize + xOffset, yOffset,
                    i * 3 * cellSize + xOffset, yOffset + 9 * cellSize);

            g.drawLine(xOffset, i * 3 * cellSize + yOffset,
                    xOffset + 9 * cellSize, i * 3 * cellSize + yOffset);
        }
    }

    public void addControls() {

        // Gets mouse inputs
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();

                int i = (x - xOffset) / cellSize;
                int j = (y - yOffset) / cellSize;

                if (i < Cell.clim && j < Cell.rlim) {
                    highlightCell = Board.getCell(i, j);
                    paintImmediately(getBounds());
                } else {
                    highlightCell = new Cell(-1, -1);
                    paintImmediately(getBounds());
                    setFocusable(true);
                    requestFocusInWindow();
                }
            }
        });

        // Gets keyboard input
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                int num = 0;

                try {

                    if (keyChar == 'q')
                        System.exit(1);
                    num = Integer.parseInt(String.valueOf(keyChar));

                    if (highlightCell.possibleValues.contains(num) || num == 0) {
                        // Allows the cell to only have one of the possible values
                        highlightCell.value = num;
                    }

                    board.updateBoard();
                    paintImmediately(getBounds());

                } catch (NumberFormatException nfe) {
                    System.out.println("Not a number!");
                } catch (NullPointerException ne) {
                    System.out.println("Nullpointer error in keylistener!!!");
                }

            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    // Displays the possible values for a cell inside the cell
    public void displayPossibleValues(Cell cell, Graphics g, int x, int y) {
        int row = -1, rowSize = 20;
        int col = 0, colSize = 20;

        g.setColor(Color.MAGENTA);

        for (int i = 0; i < cell.possibleValues.size(); i++) {
            if (i % 3 == 0)
                row = (row + 1) % 3;

            g.drawString(String.valueOf(cell.possibleValues.get(i)),
                    (int) (x + (col * colSize) + xOffset), (int) (y + (row * rowSize) + yOffset * 2));

            col = (col + 1) % 3;
        }
    }

}