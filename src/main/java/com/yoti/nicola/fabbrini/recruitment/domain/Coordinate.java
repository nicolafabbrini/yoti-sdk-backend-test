package com.yoti.nicola.fabbrini.recruitment.domain;

import javax.persistence.*;

/**
 * JPA representation of a coordinate
 * A coordinate can represent a start/end position of the roomba or the position of a patch.
 *
 * I decided to include the isPatch attribute because i didn't want to have a @{@link Roomba} table with start_position_x,
 * start_position_y, end_position_x and end_position_y because i needed a coordinate table for the patches anyway
 * and it makes sense to reuse as much of it as possible. This value allows me to filter out start and end position
 * directly when the room is loaded from the db @{@link org.hibernate.annotations.WhereJoinTable}
 *
 * Ultimately a flat table would have been slightly faster but i preferred this representation as it is cleaner for
 * the purpose of an interview with limited amount of data persisted in the database.
 */
@Entity
public class Coordinate {

    @Id
    @GeneratedValue
    private long id;
    private int x;
    private int y;
    private boolean isPatch;

    public Coordinate() {}

    public Coordinate(final int x, final int y, final boolean isPatch) {
        this.x = x;
        this.y = y;
        this.isPatch = isPatch;
    }

    public Coordinate(final Coordinate coordinate) {
        this(coordinate.getX(), coordinate.getY(), coordinate.isPatch);
    }

    public Coordinate(final int x, final int y) {
        this(x, y, false);
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public boolean isPatch() {
        return isPatch;
    }

    public void setPatch(final boolean patch) {
        isPatch = patch;
    }

    @Override
    public boolean equals(final Object o) {
        if(!(o instanceof Coordinate))
            return false;

        final Coordinate coordinate = (Coordinate)o;
        return (this.x == coordinate.getX()) && (this.y == coordinate.getY());
    }
}
