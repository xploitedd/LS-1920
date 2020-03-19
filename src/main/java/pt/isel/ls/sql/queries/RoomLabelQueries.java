package pt.isel.ls.sql.queries;

import pt.isel.ls.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class RoomLabelQueries extends DatabaseQueries {

    public RoomLabelQueries(Connection conn) {
        super(conn);
    }

    public void addRoomLabel(List<Integer> lids, int rid) throws Throwable {

        for (int lid : lids) {
            //insert rid-lid pairs into ROOM_LABEL
            PreparedStatement rl = conn.prepareStatement(
                    "INSERT INTO room_label (lid,rid) VALUES (?,?);"
            );

            rl.setInt(1,lid);
            rl.setInt(2,rid);
            rl.execute();
        }

    }

    public Iterable<Room> getLabeledRooms(int lid) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM room WHERE rid IN (SELECT rid FROM room_label WHERE lid = ?)"
        );

        stmt.setInt(1, lid);
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
