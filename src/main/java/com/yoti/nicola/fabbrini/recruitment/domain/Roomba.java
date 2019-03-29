package com.yoti.nicola.fabbrini.recruitment.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * JPA representation of a Roomba
 * A roomba contains its starting and final position, the instructions that it is going to follow and the number of
 * patches that it cleaned.
 */
@Entity
public class Roomba {

    public static final String NORTH = "N";
    public static final String SOUTH = "S";
    public static final String WEST = "W";
    public static final String EAST = "E";

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn
    @NotNull
    private Coordinate startPosition;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn
    private Coordinate endPosition;
    @NotNull
    private String instructions;
    private int cleanedPatchesCount;

    public Roomba() {}

    public Roomba(final Coordinate startPosition, final String instructions) {
        this.startPosition = startPosition;
        this.endPosition = null;
        this.instructions = instructions;
        this.cleanedPatchesCount = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Coordinate getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(final Coordinate startPosition) {
        this.startPosition = startPosition;
    }

    public Coordinate getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(final Coordinate endPosition) {
        this.endPosition = endPosition;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(final String instructions) {
        this.instructions = instructions;
    }

    public int getCleanedPatchesCount() {
        return cleanedPatchesCount;
    }

    public void setCleanedPatchesCount(final int cleanedPatchesCount) {
        this.cleanedPatchesCount = cleanedPatchesCount;
    }

    public void increasePatchCount() {
        this.cleanedPatchesCount++;
    }
}
