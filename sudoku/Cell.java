package sudoku;

import java.io.Serializable;
import java.util.ArrayList;

public class Cell implements Serializable {

    public int r;
    public int c;
    public int value;
    private static int boardSize = 9;
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

    public static void setBoadrSize(int size) {
        boardSize = size;
    }

    /* Initializes all possible values for a cell */
    public void init() {
        possibleValues.clear();
        for (int i = 1; i <= boardSize; i++) {
            possibleValues.add(i);
        }
    }
}
