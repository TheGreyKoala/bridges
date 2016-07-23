package de.feu.ps.bridges.model;

/**
 * Position in the puzzle.
 * @author Tim Gremplewski
 */
public class Position {
    private final int column;
    private final int row;

    /**
     * Creates a new instance.
     * @param column the column of the position
     * @param row thr row of the position.
     * @throws IllegalArgumentException if <code>column</code> or <code>row</code> is null.
     */
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

    /**
     * Get the column of this position.
     * @return the column of this position.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Get the row of this position.
     * @return the row of this position.
     */
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
