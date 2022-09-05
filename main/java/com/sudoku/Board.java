package com.sudoku;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Board {

    public static ArrayList<ArrayList<Cell>> grid = new ArrayList<>();
    public String difficulty = "Easy";
    public static Random r = new Random();

    public Board() {

        ArrayList<Cell> row;

        for (int i = 0; i < Cell.rlim; i++) {

            row = new ArrayList<>();

            // Initializes all cells with 0 value
            for (int j = 0; j < Cell.clim; j++)
                row.add(new Cell(i, j, 0));

            grid.add(row);
        }
    }

    // returns a particular cell in the grid
    public static Cell getCell(int r, int c) {
        return grid.get(r).get(c);
    }

    // Returns the subsquare that a cell is contained in
    public static ArrayList<ArrayList<Cell>> getSquare(Cell cell) {
        ArrayList<ArrayList<Cell>> subSquare = new ArrayList<>();
        ArrayList<Cell> row;
        int r = cell.r / 3;
        int c = cell.c / 3;

        for (int i = 0; i < 3; i++) {
            row = new ArrayList<>();

            for (int j = 0; j < 3; j++) {
                row.add(Board.getCell(r * 3 + i, c * 3 + j));
            }

            subSquare.add(row);
        }

        return subSquare;
    }

    public void clearBoard() {
        for (int i = 0; i < Cell.rlim; i++) {
            for (int j = 0; j < Cell.clim; j++) {

                Cell c = Board.getCell(i, j);
                c.init();
                c.value = 0;
            }
        }
    }

    // Updates the possible values for all the cells
    // Called after a change is made to the grid
    public void updateBoard() {
        for (int i = 0; i < Cell.rlim; i++) {
            for (int j = 0; j < Cell.clim; j++) {
                Cell cell = getCell(i, j);
                cell.init();
                cell.updateCellPossibilities();
            }
        }
    }

    // Selects a random cell from the list of cells with the least possible values
    // Assigns it a random value form its possible values
    public boolean collapseBoard() {

        Cell minCell = null, cell;
        ArrayList<Cell> collapsable = new ArrayList<>();
        int count, min = 10, filled = 0;
        boolean unsolvable = false;

        // Loop to find the min number of possible values
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell = getCell(i, j);
                count = cell.possibleValues.size();

                if (count < min && cell.value < 1) {
                    min = count;
                    if (min == 0) { // Checks if board can be solved
                        unsolvable = true; // If no possible valus for an unfilled cell,
                        break; // board cannot be solve
                    }
                }

                if (cell.value > 0)
                    filled++;
            }
        }

        if (unsolvable) {
            return false;
        }
        if (filled == 81) { // Checks if finished
            return false;
        }

        // Finds all cells with min possible values
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell = getCell(i, j);
                count = cell.possibleValues.size();

                if (count == min && cell.value < 1) {
                    collapsable.add(cell);
                }
            }
        }

        // Randomly selects a cell and assigns it a random possible value
        minCell = collapsable.get(r.nextInt(collapsable.size()));
        minCell.value = minCell.possibleValues.get(r.nextInt(minCell.possibleValues.size()));
        return true;
    }

    // Generates puzzle with a given difficulty
    public void generatePuzzle() {
        boolean isValid = true;
        boolean generated = false;
        int fillCount = 0;
        ArrayList<Integer> hiddenCells = new ArrayList<>();

        while (!generated) {

            this.clearBoard();
            fillCount = 0;
            isValid = true;

            while (isValid) {
                isValid = this.collapseBoard();
                this.updateBoard();
                fillCount++;
            }

            if (fillCount == 82) {
                generated = true;
            }
        }

        for (int i = 0; i < 81; i++)
            hiddenCells.add(i);

        ButtonPanel.saveState();

        if (difficulty == "Easy") {

            for (int i = 0; i < 45; i++) {
                int index = r.nextInt(80 - i);
                hiddenCells.remove(index);
            }
        } else if (difficulty == "Medium") {

            for (int i = 0; i < 25; i++) {
                int index = r.nextInt(80 - i);
                hiddenCells.remove(index);
            }
        } else if (difficulty == "Hard") {

            for (int i = 0; i < 10; i++) {
                int index = r.nextInt(80 - i);
                hiddenCells.remove(index);
            }
        }

        Iterator<Integer> iter = hiddenCells.iterator();
        while (iter.hasNext()) {
            int index = iter.next();
            Board.getCell(index / 9, index % 9).value = 0;
        }
        this.updateBoard();
    }

    public int finished() {

        boolean unfilled = false;

        for(int i = 0; i < 9; i++) {
            for(int j =  0; j < 9; j++) {

                Cell cell = Board.getCell(i, j);
                
                if(cell.value == 0 && cell.possibleValues.size() == 0) {
                    return -1;  // Unsolvable
                }
                else if (cell.value == 0) {
                    unfilled = true;  // Solvable 
                }
            }
        }

        if(unfilled) return 0;
            return 1; // Already solved
    }
}
