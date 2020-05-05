package pt.isel.ls.model;

import java.util.Objects;

public final class Room {

    private static final String NO_DESCRIPTION = "No Description";

    private final int rid;
    private final String name;
    private final int capacity;
    private final String description;
    private final String location;

    /**
     * Creates a new Room
     * @param rid id of the room
     * @param name name of the room
     * @param capacity capacity of the room
     * @param description description of the room
     * @param location location of the room
     */
    public Room(int rid, String name, int capacity, String description, String location) {
        this.rid = rid;
        this.name = name;
        this.capacity = capacity;
        this.description = description;
        this.location = location;
    }

    /**
     * Get Room Id
     * @return Room Id
     */
    public int getRid() {
        return rid;
    }

    /**
     * Get Room Name
     * @return Room Name
     */
    public String getName() {
        return name;
    }

    /**
     * Get Room Capacity
     * @return Room Capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Get Room Description
     * If no description is set it returns "No Description"
     * @return Room Description
     */
    public String getDescription() {
        return description == null ? NO_DESCRIPTION : description;
    }

    /**
     * Get Room Location
     * @return Room Location
     */
    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return rid + " " + name + ": capacity=" + capacity + ", description="
                + description + ", location=" + location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Room room = (Room) o;
        return rid == room.rid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rid);
    }

}