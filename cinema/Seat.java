package cinema;

public class Seat {
    private int row;
    private int column;
    private int price;

    public Seat() {}

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        setPrice(row);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int row) {
        if (row <= 4) {
            this.price = 10;
        } else {
            this.price = 8;
        }
    }
}
