package sudoku;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SudokuPanel extends JPanel {

    private int cellSize = 10;
    // private int size = Cell.size;
    // private int col = Cell.size;
    private int offset = 10;
    // private int offset = 10;
    private int subSquareSize = 3;
    private Cell highlightCell = new Cell(-1, -1);
    private Font mainFont = new Font("TimesRoman", Font.PLAIN, 25);
    private Font subFont = new Font("Serif", Font.PLAIN, 10);
    private Board board;
    public boolean displayHints = false;

    SudokuPanel() {
        addControls();
    }

    public void setBoard(Board board) {
        this.board = board;
        subSquareSize = (int) Math.sqrt(board.boardSize);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        this.removeAll();

        int width = this.getWidth();
        int height = this.getHeight();
        int smaller = width < height ? width : height;
        this.cellSize = (smaller - 2 * offset) / board.boardSize;
        // System.out.println(width);
        // System.out.println(height);
        // System.out.println(smaller);
        // System.out.println(cellSize);

        mainFont = mainFont.deriveFont((float) ((cellSize / 80.0) * 25));
        subFont = subFont.deriveFont((float) ((cellSize / 80.0) * 10));

        g.clearRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        for (int i = 0; i < board.boardSize; i++) {
            for (int j = 0; j < board.boardSize; j++) {

                int x = i * cellSize + offset;
                int y = j * cellSize + offset;
                Cell cell = board.getCell(i, j);

                if (cell == highlightCell)                  /* Draws the highlighted cell */
                    g.setColor(Color.LIGHT_GRAY);
                else
                    g.setColor(Color.BLACK);

                g.fillRect(x, y, cellSize, cellSize);

                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);       /* Draws the small cell */
                g.setColor(Color.RED);
                g.setFont(mainFont);

                if (cell.value != 0)                        /* Writes the value of the cell */
                    g.drawString(String.valueOf(cell.value), (int) (x + cellSize / 2), (int) (y + cellSize / 2));

                g.setFont(subFont);
                if (displayHints)
                    displayPossibleValues(cell, g, x, y);   /* Displays the possible values of a cell */
            }
        }

        g.setColor(Color.WHITE);

        /* Divides the grid into squares */
        for (int i = 1; i < board.boardSize; i++) {
            g.drawLine(i * cellSize + offset, offset,
                    i * cellSize + offset, offset + board.boardSize * cellSize);

            g.drawLine(offset, i * cellSize + offset,
                    offset + board.boardSize * cellSize, i * cellSize + offset);
        }

        g.setColor(Color.YELLOW);

        /* Divides the grid into subsquares */
        for (int i = 1; i < subSquareSize; i++) {
            g.drawLine(i * subSquareSize * cellSize + offset, offset,
                    i * subSquareSize * cellSize + offset, offset + board.boardSize * cellSize);

            g.drawLine(offset, i * subSquareSize * cellSize + offset,
                    offset + board.boardSize * cellSize, i * subSquareSize * cellSize + offset);
        }
    }

    public void addControls() {

        /* Gets mouse inputs */
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();

                int i = (x - offset) / cellSize;
                int j = (y - offset) / cellSize;

                if (i < board.boardSize && j < board.boardSize) {
                    highlightCell = board.getCell(i, j);
                    paintImmediately(getBounds());
                } else {
                    highlightCell = new Cell(-1, -1);
                    paintImmediately(getBounds());
                    setFocusable(true);
                    requestFocusInWindow();
                }
            }
        });

        /* Gets keyboard input */
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
                        highlightCell.value = num;      /* Allows the cell to only have one of the possible values */
                    }

                    int finished = board.finished();

                    board.updateBoard();
                    paintImmediately(getBounds());

                    if (finished == 1)
                        JOptionPane.showMessageDialog(null, "Solved!");

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

    /* Displays the possible values for a cell inside the cell */
    public void displayPossibleValues(Cell cell, Graphics g, int x, int y) {
        int row = -1, rowSize = 20;
        int col = 0, colSize = 20;

        g.setColor(Color.MAGENTA);

        for (int i = 0; i < cell.possibleValues.size(); i++) {
            if (i % subSquareSize == 0)
                row = (row + 1) % subSquareSize;

            g.drawString(String.valueOf(cell.possibleValues.get(i)),
                    (int) (x + (col * colSize) + offset), (int) (y + (row * rowSize) + offset * 2));

            col = (col + 1) % subSquareSize;
        }
    }

}
