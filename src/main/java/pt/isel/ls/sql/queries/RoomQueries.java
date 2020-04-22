package pt.isel.ls.sql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.request.Parameter;

public class RoomQueries extends DatabaseQueries {

    public RoomQueries(Connection conn) {
        super(conn);
    }

    /**
     * Creates a new Room
     * @param name name of the room
     * @param location location of the room
     * @param capacity capacity of the room
     * @param description room description (optional)
     * @param labels list of room labels
     * @return the created room
     * @throws Throwable any exception that occurs
     */
    public Room createNewRoom(String name, String location, int capacity,
                              String description, List<Label> labels) throws Throwable {

        //Inserts the new Room
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO room (name, location, capacity) VALUES (?, ?, ?);");

        stmt.setString(1,name);
        stmt.setString(2,location);
        stmt.setInt(3,capacity);
        stmt.execute();

        //Verifies if the new Room has been inserted
        Room toReturn = getRoom(name, location, capacity);

        RoomLabelQueries rlQuery = new RoomLabelQueries(conn);
        rlQuery.addRoomLabels(labels, toReturn.getRid());

        if (description != null) { //Associate description with new Room
            PreparedStatement din = conn.prepareStatement(
                    "INSERT INTO description (rid, description) VALUES (?,?);"
            );

            din.setInt(1, toReturn.getRid());
            din.setString(2, description);
            din.execute();
        }

        return toReturn;
    }

    /**
     * Get a room by name, location and capacity
     * @param name name of the room
     * @param location location of the room
     * @param capacity capacity of the room
     * @return the requested room
     * @throws Throwable any exception that occurs
     */
    public Room getRoom(String name, String location, int capacity) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT room.rid, description FROM room "
                        + "FULL JOIN description d on room.rid = d.rid "
                        + "WHERE name = ? AND location = ? AND capacity = ?;"
        );

        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setInt(3, capacity);

        ResultSet rs = stmt.executeQuery();
        rs.next();
        int id = rs.getInt("rid");
        String desc = rs.getString("description");

        return new Room(id, name, capacity, desc, location);
    }

    /**
     * Get Room by rid
     * @param rid id of the room
     * @return a Room
     * @throws Throwable any exception that occurs
     */
    public Room getRoom(int rid) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT room.rid, name, location, capacity, description "
                        + "FROM room FULL JOIN description d "
                        + "on room.rid = d.rid WHERE room.rid = ?;"
        );

        stmt.setInt(1,rid);

        ResultSet rs = stmt.executeQuery();
        rs.next();

        String name = rs.getString("name");
        String location = rs.getString("location");
        int capacity = rs.getInt("capacity");
        String desc = rs.getString("description");

        return new Room(rid, name, capacity, desc, location);
    }

    /**
     * Get all Rooms
     * @return all Rooms
     * @throws Throwable any exception that occurs
     */
    public Iterable<Room> getRooms() throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT room.rid, name, location, capacity, description "
                        + "FROM room FULL JOIN description d on room.rid = d.rid;"
        );

        ResultSet rs = stmt.executeQuery();

        LinkedList<Room> rooms = new LinkedList<>();
        while (rs.next()) {
            int rid = rs.getInt("rid");
            String name = rs.getString("name");
            String location = rs.getString("location");
            int capacity = rs.getInt("capacity");
            String desc = rs.getString("description");
            rooms.add(new Room(rid, name, capacity, desc, location));
        }

        return rooms;
    }

    public Iterable<Room> getRooms(Optional<List<Parameter>> begin,
                                   Optional<List<Parameter>> duration,
                                   Optional<List<Parameter>> capacity,
                                   Optional<List<Parameter>> label) throws Throwable {
        boolean byTime = false;
        boolean byCapacity = false;
        boolean byLabel = false;

        if (begin.isPresent() && duration.isPresent()) {
            PreparedStatement byTimeStmt = conn.prepareStatement(
                    "CREATE VIEW ROOMS_BY_TIME AS SELECT * FROM room where room.rid "
                    + "NOT IN (SELECT rid FROM booking WHERE begin >= ? and \"end\" <= ?)"
            );

            long b = begin.get().iterator().next().toLong();
            long d = duration.get().iterator().next().toLong();

            byTimeStmt.setTimestamp(1, new Timestamp(b));
            byTimeStmt.setTimestamp(2, new Timestamp(b + d));

            byTimeStmt.execute();
            byTime = true;
        }

        if (capacity.isPresent()) {
            PreparedStatement byCapacityStmt = conn.prepareStatement(
                    "CREATE VIEW ROOMS_BY_CAPACITY AS SELECT * FROM room WHERE capacity >= ?");

            byCapacityStmt.setInt(1, capacity.get().iterator().next().toInt());

            byCapacityStmt.execute();
            byCapacity = true;
        }

        if (label.isPresent()) {
            String byLabelString = "CREATE VIEW ROOMS_BY_LABEL AS SELECT * FROM room "
                    + "WHERE room.rid IN (SELECT rid FROM room_label WHERE lid = ?)"
                    + "AND room.rid IN (SELECT rid FROM room_label WHERE lid = ?)".repeat(label.get().size() - 1) + ";";

            PreparedStatement byLabelStmt = conn.prepareStatement(byLabelString);

            int i = 0;
            for (Parameter lbl : label.get()) {
                byLabelStmt.setInt(++i, new LabelQueries(conn).getLabel(lbl.toString()).getLid());
            }

            byLabelStmt.execute();
            byLabel = true;
        }

        PreparedStatement finalStmt = conn.prepareStatement(
                "SELECT * FROM (" + (byTime ? "ROOMS_BY_TIME" : "room" + " INTERSECT ")
                        + (byCapacity ? "ROOMS_BY_CAPACITY" : "room")
                        + " INTERSECT " + (byLabel ? "ROOMS_BY_LABEL" : "room") + ");");

        ResultSet rs = finalStmt.executeQuery();

        LinkedList<Room> rooms = new LinkedList<>();
        while (rs.next()) {
            int rid = rs.getInt("rid");
            String name = rs.getString("name");
            String location = rs.getString("location");
            int cap = rs.getInt("capacity");
            String desc = rs.getString("description");
            rooms.add(new Room(rid, name, cap, desc, location));
        }

        return rooms;
    }
}
