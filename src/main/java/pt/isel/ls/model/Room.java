package pt.isel.ls.model;

public class Room {

    private final int rid;
    private final String name;
    private final String description;
    private final int capacity;
    private final String location;

    public Room(int rid, String name, int capacity, String description, String location) {
        this.rid = rid;
        this.name = name;
        this.capacity = capacity;
        this.description = description;
        this.location = location;
    }

    public int getRid() {
        return rid;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

}