package com.sudoku;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Cell implements Serializable {

    public int r;
    public int c;
    public int value;
    public static int rlim = 9;
    public static int clim = 9;
    public static int valueLimit = 9;
    private static Board board;
    public ArrayList<Integer> possibleValues = new ArrayList<>();

    public Cell(int a, int b) {
        this.r = a;
        this.c = b;
        init();
    }

    public Cell(int a, int b, int val) {
        this(a, b);
        this.value = val;
    }

    // Initializes all possible values for a cell
    public void init() {
        possibleValues.clear();
        for (int i = 1; i <= valueLimit; i++) {
            possibleValues.add(i);
        }
    }

    public static void setBoard(Board board) {
        Cell.board = board;
    }

    // Updates the possible values for a cell based on the 3 sudoku constraints
    public void updateCellPossibilities() {

        for (int i = 0; i < Cell.rlim; i++) {

            Cell c1 = Board.getCell(this.r, i);
            Cell c2 = Board.getCell(i, this.c);

            if (this.possibleValues.contains(c1.value)) // Row Constraint
                this.possibleValues.remove(this.possibleValues.indexOf(c1.value));
            if (this.possibleValues.contains(c2.value)) // Column Constaint
                this.possibleValues.remove(this.possibleValues.indexOf(c2.value));
        }

        for (ArrayList<Cell> array : Board.getSquare(this)) {
            Iterator<Cell> iter = array.iterator();

            while (iter.hasNext()) {
                int val = iter.next().value;
                if (this.possibleValues.contains(val)) // Sub square Constraint
                    this.possibleValues.remove(this.possibleValues.indexOf(val));
            }
        }
    }

    public ArrayList<Cell> getNeighbours() {

        ArrayList<Cell> allNeighbours = new ArrayList<>();
        ArrayList<Cell> validNeighbours = new ArrayList<>();

        allNeighbours.add(new Cell(r + 1, c));
        allNeighbours.add(new Cell(r, c + 1));
        allNeighbours.add(new Cell(r - 1, c));
        allNeighbours.add(new Cell(r, c - 1));
        allNeighbours.add(new Cell(r + 1, c + 1));
        allNeighbours.add(new Cell(r - 1, c - 1));
        allNeighbours.add(new Cell(r + 1, c - 1));
        allNeighbours.add(new Cell(r - 1, c + 1));

        Iterator<Cell> iterator = allNeighbours.iterator();

        while (iterator.hasNext()) {
            Cell cell = iterator.next();

            if (cell.r < 0 || cell.c < 0 || cell.r > rlim - 1 || cell.c > clim - 1)
                continue;
            else
                validNeighbours.add(Board.getCell(cell.r, cell.c));
        }

        return validNeighbours;
    }
}
