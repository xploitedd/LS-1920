package pt.isel.ls.sql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import pt.isel.ls.model.Room;

public class RoomQueries extends DatabaseQueries {

    public RoomQueries(Connection conn) {
        super(conn);
    }

    public Room createNewRoom(String name, String location, int capacity,
                              Optional<List<String>> description, Optional<List<String>> labels) throws Throwable {

        List<Integer> lids = new LinkedList<>();

        if (labels.isPresent()) { //Gets List of labels to associate with room
            LabelQueries lblQuery = new LabelQueries(conn);
            for (String lbl : labels.get()) {
                lids.add(lblQuery.getLabel(lbl).getLid());
            }
        }
        //Inserts the new Room
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO room (name, location, capacity) VALUES (?,?,?);");

        stmt.setString(1,name);
        stmt.setString(2,location);
        stmt.setInt(3,capacity);
        stmt.execute();
        //Verifies if the new Room has been inserted
        Room toReturn = getRoom(name, location, capacity);

        RoomLabelQueries rlQuery = new RoomLabelQueries(conn);
        rlQuery.addRoomLabel(lids,toReturn.getRid());

        if (description.isPresent()) { //Associate Description with new Room
            String d = description.get().get(0);
            PreparedStatement din = conn.prepareStatement(
                    "INSERT INTO description (rid, description) VALUES (?,?);"
            );
            din.setInt(1,toReturn.getRid());
            din.setString(2,d);
            din.execute();
        }

        return toReturn;
    }

    public Room getRoom(String name, String location, int capacity) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT room.rid, name, location, capacity, description FROM room "
                        + "FULL JOIN description d on room.rid = d.rid "
                        + "WHERE name = ? AND location = ? AND capacity = ?;"
        );
        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setInt(3, capacity);

        ResultSet rs = stmt.executeQuery();
        rs.next();
        int id = rs.getInt("rid");
        String rsName = rs.getString("name");
        String rsLocation = rs.getString("location");
        int rsCapacity = rs.getInt("capacity");
        String desc = rs.getString("description");

        return new Room(id, rsName, rsCapacity, desc, rsLocation);
    }

    public Room getRoom(int rid) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT room.rid, name, location, capacity, description "
                        + "FROM room FULL JOIN description d on room.rid = d.rid WHERE room.rid = ?;"
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

}
