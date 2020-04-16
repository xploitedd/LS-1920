package pt.isel.ls.sql.queries;

import pt.isel.ls.model.Label;
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

    /**
     * Associate labels to a room
     * @param labels labels to be associated
     * @param rid room that will receive the labels
     * @throws Throwable any exception that occurs
     */
    public void addRoomLabels(List<Label> labels, int rid) throws Throwable {
        if (labels.size() == 0) {
            return;
        }
        String insertStatement = "INSERT INTO room_label (lid, rid) VALUES (?, ?)"
                + ", (?, ?)".repeat(labels.size() - 1) + ';';
        PreparedStatement rl = conn.prepareStatement(insertStatement);
        int i = 0;
        for (Label label : labels) {
            //insert rid-lid pairs into ROOM_LABEL
            rl.setInt(++i, label.getLid());
            rl.setInt(++i, rid);
        }
        rl.execute();
    }

    /**
     * Get all rooms that have a specified label
     * @param lid label to filter for
     * @return all rooms with the label
     * @throws Throwable any exception that occurs
     */
    public Iterable<Room> getLabeledRooms(int lid) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT r.rid, \"name\", location, capacity, description "
                        + "FROM (room r FULL JOIN description d on r.rid = d.rid) "
                        + "WHERE r.rid IN (SELECT rid FROM room_label WHERE lid = ?)"
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

    /**
     * Get all labels that are associated to a room
     * @param rid room containing the labels
     * @return all labels that are on a room
     * @throws Throwable any exception that occurs
     */
    public Iterable<Label> getRoomLabels(int rid) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT l.lid, name FROM "
                        + "(room_label rl JOIN label l on rl.lid = l.lid) WHERE rid = ?"
        );

        stmt.setInt(1, rid);

        ResultSet rs = stmt.executeQuery();
        LinkedList<Label> labels = new LinkedList<>();
        while (rs.next()) {
            labels.add(new Label(rs.getInt("lid"),
                    rs.getString("name")));
        }

        return labels;
    }

}
