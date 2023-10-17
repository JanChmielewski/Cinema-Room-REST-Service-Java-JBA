package cinema;

import java.util.ArrayList;
import java.util.Arrays;

public class Room {
    private int columns;
    private int rows;
    private ArrayList<Seat> seats;

    Room() {}
    Room(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.seats = getAvailableSeats(columns, rows);
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public ArrayList<Seat> getAvailableSeats(int columns, int rows) {
        ArrayList<Seat> seats = new ArrayList<>();
        for (int row = 1; row <= rows; row++) {
            for (int column = 1; column <= columns; column++) {
                seats.add(new Seat(row, column));
            }
        }
        return seats;
    }
}
