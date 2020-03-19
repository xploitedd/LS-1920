package pt.isel.ls.sql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import pt.isel.ls.model.Room;

public class RoomQueries extends DatabaseQueries {

    public RoomQueries(Connection conn) {
        super(conn);
    }

    public Room createNewRoom(int rid, String name, String location, int capacity, String description, String label) throws Throwable {

        return null;
    }

    public Room getRoom(int rid) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM room WHERE rid = ?");
        stmt.setInt(1,rid);

        ResultSet rs = stmt.executeQuery();
        rs.next();
        int id = rs.getInt("rid");
        String name = rs.getString("name");
        String location = rs.getString("location");
        int capacity = rs.getInt("capacity");
        String desc = rs.getString("description");

        return new Room(id, name, capacity, desc, location);
    }

    public Iterable<Room> getRooms() throws Throwable {
        PreparedStatement stmt = conn.prepareStatement("SELECT room.rid, name, location, capacity, description FROM room FULL JOIN description d on room.rid = d.rid");
        ResultSet rs = stmt.executeQuery();

        LinkedList<Room> results = new LinkedList<>();
        while (rs.next()) {
            int rid = rs.getInt("rid");
            String name = rs.getString("name");
            String location = rs.getString("location");
            int capacity = rs.getInt("capacity");
            String desc = rs.getString("description");
            results.add(new Room(rid, name, capacity, desc, location));
        }

        return results;
    }

}
