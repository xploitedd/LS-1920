package pt.isel.ls.sql.queries;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.sql.api.SqlHandler;

public class RoomQueries extends DatabaseQueries {

    public RoomQueries(SqlHandler handler) {
        super(handler);
    }

    /**
     * Creates a new Room
     * @param name name of the room
     * @param location location of the room
     * @param capacity capacity of the room
     * @param description room description (optional)
     * @param labels list of room labels
     * @return the created room
     */
    public Room createNewRoom(String name, String location, int capacity,
                              String description, List<Label> labels) {

        // Inserts the new Room
        handler.createUpdate("INSERT INTO room (name, location, capacity) VALUES (?, ?, ?);")
                .bind(name)
                .bind(location)
                .bind(capacity)
                .execute();

        // Verifies if the new Room has been inserted
        Room toReturn = getRoom(name, location, capacity);
        RoomLabelQueries rlQuery = new RoomLabelQueries(handler);
        rlQuery.addRoomLabels(labels, toReturn.getRid());

        if (description != null) {
            //Associate description with new Room
            handler.createUpdate("INSERT INTO description (rid, description) VALUES (?,?);")
                    .bind(toReturn.getRid())
                    .bind(description)
                    .execute();
        }

        return toReturn;
    }

    /**
     * Get a room by name, location and capacity
     * @param name name of the room
     * @param location location of the room
     * @param capacity capacity of the room
     * @return the requested room
     */
    public Room getRoom(String name, String location, int capacity) {
        Optional<Room> room = handler
                .createQuery("SELECT room.rid, name, capacity, description, location FROM room "
                + "FULL JOIN description d on room.rid = d.rid "
                + "WHERE name = ? AND location = ? AND capacity = ?;")
                .bind(name)
                .bind(location)
                .bind(capacity)
                .mapToClass(Room.class)
                .findFirst();

        if (room.isEmpty()) {
            throw new RouteException("No room found", StatusCode.NOT_FOUND);
        }

        return room.get();
    }

    /**
     * Get Room by rid
     * @param rid id of the room
     * @return a Room
     */
    public Room getRoom(int rid) {
        Optional<Room> room = handler
                .createQuery("SELECT room.rid, name, location, capacity, description "
                    + "FROM room FULL JOIN description d "
                    + "on room.rid = d.rid WHERE room.rid = ?;")
                .bind(rid)
                .mapToClass(Room.class)
                .findFirst();

        if (room.isEmpty()) {
            throw new RouteException("No room found with id " + rid, StatusCode.NOT_FOUND);
        }

        return room.get();
    }

    /**
     * Get all Rooms
     * @return all Rooms
     */
    public Stream<Room> getRooms() {
        return handler.createQuery("SELECT room.rid, name, location, capacity, description "
                + "FROM room FULL JOIN description d on room.rid = d.rid;")
                .mapToClass(Room.class);
    }
}
