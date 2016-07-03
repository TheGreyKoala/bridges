package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
public class Position {
    private final int column;
    private final int row;

    public Position(int column, int row) {
        if (column < 0) {
            throw new IllegalArgumentException("Parameter 'column' must not be less than 0.");
        }

        if (row < 0) {
            throw new IllegalArgumentException("Parameter 'row' must not be less than 0.");
        }

        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;

        Position position = (Position) o;

        if (column != position.column) return false;
        return row == position.row;
    }

    @Override
    public int hashCode() {
        int result = column;
        result = 31 * result + row;
        return result;
    }
}
