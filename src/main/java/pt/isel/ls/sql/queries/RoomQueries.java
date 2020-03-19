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

    public Room createNewRoom(int rid, String name, String location, int capacity, String description, Optional<List<String>> labels) throws Throwable {
        List<Integer> lids = new LinkedList<>();
        if (labels.isPresent()) { //Gets List of labels to associate with room
            for (String lbl : labels.get()) {
                PreparedStatement ls = conn.prepareStatement(
                        "SELECT lid FROM label WHERE name = ?;"
                );

                ls.setString(1,lbl);
                ResultSet rls = ls.executeQuery();
                int lid = -1;
                if (rls.next()) {
                    rls.getInt("lid");
                } else {
                    throw new IllegalArgumentException("Label '" + lbl + "' not found, aborted creation of room");
                }
                lids.add(lid);
            }
        }

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO room (name, location, capacity) VALUES (?,?,?);"
        );

        stmt.setString(1,name);
        stmt.setString(2,location);
        stmt.setInt(3,capacity);
        stmt.execute();

        Room toReturn = getRoom(rid);

        for (int lid : lids) {
            //insert rid-lid pairs into ROOM_LABEL
            PreparedStatement rl = conn.prepareStatement(
                    "INSERT INTO room_label (lid,rid) VALUES (?,?);"
            );

            rl.setInt(1,lid);
            rl.setInt(2,rid);
            rl.execute();
        }

        return toReturn;
    }

    public Room getRoom(int rid) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM room WHERE rid = ?;"
        );
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
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT room.rid, name, location, capacity, description FROM room FULL JOIN description d on room.rid = d.rid;"
        );
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
