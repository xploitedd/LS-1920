package pt.isel.ls.sql.queries;

import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.api.Search;
import pt.isel.ls.sql.api.SqlHandler;
import pt.isel.ls.sql.api.Update;

import java.util.List;
import java.util.stream.Stream;

import static pt.isel.ls.utils.ExceptionUtils.passException;

public class RoomLabelQueries extends DatabaseQueries {

    public RoomLabelQueries(SqlHandler handler) {
        super(handler);
    }

    /**
     * Associate labels to a room
     * @param labels labels to be associated
     * @param rid room that will receive the labels
     */
    public void addRoomLabels(List<Label> labels, int rid) {
        if (labels.size() == 0) {
            return;
        }

        Update update = handler.createUpdate("INSERT INTO room_label (lid, rid) VALUES (?, ?)"
                + ", (?, ?)".repeat(labels.size() - 1) + ';');

        for (Label label : labels) {
            //insert rid-lid pairs into ROOM_LABEL
            update = update.bind(label.getLid())
                    .bind(rid);
        }

        update.execute();
    }

    /**
     * Get all rooms that have a specified label
     * @param lid label to filter for
     * @return all rooms with the label
     */
    public Stream<Room> getLabeledRooms(int lid) {
        if (!doesLabelExist(lid)) {
            throw new RouteException("A label with id " + lid + " does not exist!");
        }

        return handler.createQuery("SELECT r.rid, \"name\", location, capacity, description "
                        + "FROM (room r FULL JOIN description d on r.rid = d.rid) "
                        + "WHERE r.rid IN (SELECT rid FROM room_label WHERE lid = ?)")
                .bind(lid)
                .mapToClass(Room.class);
    }

    /**
     * Get all labels that are associated to a room
     * @param rid room containing the labels
     * @return all labels that are on a room
     */
    public Stream<Label> getRoomLabels(int rid) {
        return handler.createQuery("SELECT l.lid, name FROM "
                + "(room_label rl JOIN label l on rl.lid = l.lid) WHERE rid = ?")
                .bind(rid)
                .mapToClass(Label.class);
    }

    public Stream<Room> getRoomsWithLabels(List<String> labels) {
        int count = labels.size();
        String params = "?,".repeat(count);
        Search query = handler.createQuery("select r.rid, \"name\", location, capacity, description"
                + " from (room r join description d on r.rid = d.rid) where r.rid in ("
                + "select rid from (room_label rl join label l on rl.lid = l.lid) "
                + "where l.name in (" + params.substring(0, params.length() - 1) + ") "
                + "group by rid having count(rid) = ?);");

        for (String label : labels) {
            query.bind(label);
        }

        return query.bind(count)
                .mapToClass(Room.class);
    }

    public boolean doesLabelExist(int lid) {
        return passException(() ->
                handler.createQuery("SELECT * FROM label WHERE lid=?")
                        .bind(lid)
                        .execute()
                        .next());
    }

}
