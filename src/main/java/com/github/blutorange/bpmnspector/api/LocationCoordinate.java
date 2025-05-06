package com.github.blutorange.bpmnspector.api;

/** A location in a file with a row and column. */
public final class LocationCoordinate {
    private static final LocationCoordinate EMPTY = new LocationCoordinate(-1, -1);

    private final int row;
    private final int column;

    /**
     * Constructs a LocationCoordinate with the specified row and column.
     *
     * @param row the row of the coordinate
     * @param column the column of the coordinate
     */
    public LocationCoordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the row of the coordinate.
     *
     * @return the row of the coordinate
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of the coordinate.
     *
     * @return the column of the coordinate
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns a string representation of the coordinate in the format "row,column".
     *
     * @return a string representation of the coordinate
     */
    public String getId() {
        return String.format("%d,%d", row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var location = (LocationCoordinate) o;

        if (column != location.column) return false;
        if (row != location.row) return false;

        return true;
    }

    @Override
    public int hashCode() {
        var result = row;
        result = 31 * result + column;
        return result;
    }

    @Override
    public String toString() {
        return "Location{" + "row=" + row + ", column=" + column + '}';
    }

    /**
     * Returns an empty LocationCoordinate with row and column set to -1.
     *
     * @return an empty LocationCoordinate
     */
    public static LocationCoordinate empty() {
        return EMPTY;
    }
}
