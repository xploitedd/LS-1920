package pt.isel.ls.sql.queries;

import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.response.RouteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class RoomLabelQueries extends DatabaseQueries {

    public RoomLabelQueries(Connection conn) {
        super(conn);
    }

    /**
     * Associate labels to a room
     * @param labels labels to be associated
     * @param rid room that will receive the labels
     * @throws Exception any exception that occurs
     */
    public void addRoomLabels(List<Label> labels, int rid) throws Exception {
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
     * @throws Exception any exception that occurs
     */
    public Stream<Room> getLabeledRooms(int lid) throws Exception {
        if (!doesLabelExist(lid)) {
            throw new RouteException("A label with id " + lid + " does not exist!");
        }

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

        return rooms.stream();
    }

    /**
     * Get all labels that are associated to a room
     * @param rid room containing the labels
     * @return all labels that are on a room
     * @throws Exception any exception that occurs
     */
    public Stream<Label> getRoomLabels(int rid) throws Exception {
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

        return labels.stream();
    }

    public boolean isLabelInRoom(int rid, String label) throws Exception {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM (room_label rl JOIN label l on rl.lid=l.lid) WHERE rid=? AND l.name=?"
        );

        stmt.setInt(1, rid);
        stmt.setString(2, label);
        return stmt.executeQuery().next();
    }

    public boolean doesLabelExist(int lid) throws Exception {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM label WHERE lid=?"
        );

        stmt.setInt(1, lid);
        return stmt.executeQuery().next();
    }

}
