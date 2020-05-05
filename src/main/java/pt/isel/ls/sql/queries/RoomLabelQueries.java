package pt.isel.ls.sql.queries;

import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.api.SqlHandler;
import pt.isel.ls.sql.api.Update;

import java.util.List;
import java.util.stream.Stream;

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

        int i = 0;
        for (Label label : labels) {
            //insert rid-lid pairs into ROOM_LABEL
            update = update.bind(i++, label.getLid())
                    .bind(i++, rid);
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
                .bind(0, lid)
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
                .bind(0, rid)
                .mapToClass(Label.class);
    }

    public boolean isLabelInRoom(int rid, String label) {
        return SqlHandler.passException(() -> handler
                .createQuery("SELECT * FROM (room_label rl JOIN label l on rl.lid=l.lid) WHERE rid=? AND l.name=?")
                .bind(0, rid)
                .bind(1, label)
                .execute()
                .next());
    }

    public boolean doesLabelExist(int lid) {
        return SqlHandler.passException(() ->
                handler.createQuery("SELECT * FROM label WHERE lid=?")
                        .bind(0, lid)
                        .execute()
                        .next());
    }

}
