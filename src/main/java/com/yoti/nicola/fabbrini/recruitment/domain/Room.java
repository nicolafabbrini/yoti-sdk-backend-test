package com.yoti.nicola.fabbrini.recruitment.domain;

import org.hibernate.annotations.WhereJoinTable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA representation of a Room.
 * The room is the root class of our model, containing all the informations sent by the user.
 * An instance of the room has a specific dimension (width and height), with a number of patches and a @{@link Roomba}.
 *
 * Given the one to one association between roomba and room, all the attributes in roomba could be stored in the room
 * entity as a flat table. I choose this structure as it's more object oriented and therefore easier to read.
 *
 * I preferred to represent the dimension of the room as width and height instead of @{@link Coordinate} because it
 * makes the code more readable.
 */
@Entity
public class Room {

    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private Integer width;
    @NotNull
    private Integer height;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @WhereJoinTable(clause = "isPatch=true")
    @NotNull
    private List<Coordinate> patches;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn
    @NotNull
    private Roomba roomba;

    public Room() {}

    public Room(final int width, final int height, final List<Coordinate> patches, final Roomba roomba) {
        this.width = width;
        this.height = height;
        this.patches = new ArrayList<>(patches); // Encapsulation-safe setter
        this. roomba = roomba;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public List<Coordinate> getPatches() {
        return new ArrayList<>(patches); // Encapsulation-safe getter
    }

    public void setPatches(final List<Coordinate> patches) {
        this.patches = new ArrayList<>(patches); // Encapsulation-safe setter
    }

    public Roomba getRoomba() {
        return roomba;
    }

    public void setRoomba(final Roomba roomba) {
        this.roomba = roomba;
    }
}
