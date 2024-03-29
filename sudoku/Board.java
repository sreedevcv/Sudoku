package sudoku;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Board {

    public ArrayList<ArrayList<Cell>> grid = new ArrayList<>();
    private ArrayList<ArrayList<Cell>> savedGrid = new ArrayList<>();
    private String difficulty = "Easy";
    private Random random = new Random();
    public int boardSize = 9;

    public Board(int size) {

        this.boardSize = size;
        ArrayList<Cell> row;

        /* Initializes all cells with 0 value */
        for (int i = 0; i < boardSize; i++) {
            row = new ArrayList<>();

            for (int j = 0; j < boardSize; j++)
                row.add(new Cell(i, j, 0));
            grid.add(row);
        }
    }

    /* Returns a particular cell in the grid */
    public Cell getCell(int r, int c) {
        return grid.get(r).get(c);
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /* Returns the subsquare that a cell is contained in */
    public ArrayList<ArrayList<Cell>> getSquare(Cell cell) {
        ArrayList<ArrayList<Cell>> subSquare = new ArrayList<>();
        ArrayList<Cell> row;
        int squareSize = (int) Math.sqrt(this.boardSize);
        int r = cell.r / squareSize;
        int c = cell.c / squareSize;

        for (int i = 0; i < squareSize; i++) {
            row = new ArrayList<>();

            for (int j = 0; j < squareSize; j++) {
                row.add(getCell(r * squareSize + i, c * squareSize + j));
            }

            subSquare.add(row);
        }

        return subSquare;
    }

    public void clearBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                Cell c = getCell(i, j);
                c.init();
                c.value = 0;
            }
        }
    }

    /*
     * Updates the possible values for all the cells
     * Called after a change is made to the grid
     */
    public void updateBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Cell cell = getCell(i, j);
                cell.init();
                updateCellPossibilities(cell);
            }
        }
    }

    /*
     * Selects a random cell from the list of cells with the least possible values
     * Assigns it a random value form its possible values
     */
    public boolean collapseBoard() {

        Cell minCell = null, cell;
        ArrayList<Cell> collapsable = new ArrayList<>();
        int count, min = boardSize + 1, filled = 0;
        boolean unsolvable = false;

        /* Loop to find the cell with min number of possible values */
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                cell = getCell(i, j);

                if (cell.value > 0) {
                    filled++;
                    continue;
                }

                count = cell.possibleValues.size();

                /* If there are no possible values for an unfilled, cell, board cannot be solved */
                if (count == 0) {
                    return false;
                }

                if (count < min && cell.value == 0)
                    min = count;
            }
        }

        if (filled == boardSize * boardSize) { /* Checks if finished */
            return false;
        }

        /* Finds all cells with min possible values */
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                cell = getCell(i, j);
                count = cell.possibleValues.size();

                if (count == min && cell.value == 0) {
                    collapsable.add(cell);
                }
            }
        }

        /* Randomly selects a cell and assigns it a random possible value */
        minCell = collapsable.get(random.nextInt(collapsable.size()));
        minCell.value = minCell.possibleValues.get(random.nextInt(minCell.possibleValues.size()));
        return true;
    }

    /* Generates puzzle with a given difficulty */
    public void generatePuzzle() {
        boolean isValid = true;
        boolean generated = false;
        int fillCount = 0, hideCount = 0;
        Cell cell;

        while (!generated) {
            hideCount++;
            this.clearBoard();
            fillCount = -1;
            isValid = true;

            while (isValid) {
                isValid = this.collapseBoard();
                this.updateBoard();
                fillCount++;
            }

            if (fillCount == boardSize * boardSize) {
                generated = true;
            }

            System.out.println(hideCount + " " + fillCount);
        }

        this.saveState();

        switch (difficulty) {
            case "Easy":
                hideCount = (int) (boardSize * boardSize * 0.4);
                break;
            case "Medium":
                hideCount = (int) (boardSize * boardSize * 0.6);
                break;
            case "Hard":
                hideCount = (int) (boardSize * boardSize * 0.8);
                break;
        }

        while (hideCount > 0) {
            int r = random.nextInt(boardSize);
            int c = random.nextInt(boardSize);
            cell = getCell(r, c);

            if (cell.value != 0) {
                cell.value = 0;
                hideCount -= 1;
            }
        }

        this.updateBoard();
    }

    public int finished() {

        boolean unfilled = false;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                Cell cell = getCell(i, j);

                if (cell.value == 0 && cell.possibleValues.size() == 0) {
                    return -1; /* Unsolvable */
                } else if (cell.value == 0) {
                    unfilled = true; /* Solvable */
                }
            }
        }

        if (unfilled)
            return 0;
        return 1; /* Already solved */
    }

    /* Updates the possible values for a cell based on the 3 sudoku constraints */
    public void updateCellPossibilities(Cell cell) {

        for (int i = 0; i < boardSize; i++) {

            Cell c1 = getCell(cell.r, i);
            Cell c2 = getCell(i, cell.c);

            if (cell.possibleValues.contains(c1.value)) /* Row Constraint */
                cell.possibleValues.remove(cell.possibleValues.indexOf(c1.value));
            if (cell.possibleValues.contains(c2.value)) /* Column Constaint */
                cell.possibleValues.remove(cell.possibleValues.indexOf(c2.value));
        }

        for (ArrayList<Cell> array : getSquare(cell)) {
            Iterator<Cell> iter = array.iterator();

            while (iter.hasNext()) {
                int val = iter.next().value;
                if (cell.possibleValues.contains(val)) /* Sub square Constraint */
                    cell.possibleValues.remove(cell.possibleValues.indexOf(val));
            }
        }
    }

    /* Saves the state of the grid */
    public void saveState() {
        Iterator<ArrayList<Cell>> rowIterator = grid.iterator();
        ArrayList<Cell> row;
        Iterator<Cell> cellIterator;
        Cell cell, tempCell;

        savedGrid.clear();

        while (rowIterator.hasNext()) {
            cellIterator = rowIterator.next().iterator();
            row = new ArrayList<>();

            while (cellIterator.hasNext()) {
                tempCell = cellIterator.next();
                cell = new Cell(tempCell.r, tempCell.c, tempCell.value);
                cell.init();
                row.add(cell);
            }

            savedGrid.add(row);
        }
    }

    /* Loads the saved state of grid */
    public void loadState() {
        ArrayList<ArrayList<Cell>> temp = grid;
        grid = savedGrid;
        savedGrid = temp;
    }
}
