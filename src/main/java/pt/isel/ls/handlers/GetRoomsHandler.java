package pt.isel.ls.handlers;

import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;

import java.util.List;
import java.util.Optional;

import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.TableView;

public final class GetRoomsHandler implements RouteHandler {

    private final ConnectionProvider provider;

    public GetRoomsHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets all of the Rooms or a specific room
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        Optional<Parameter> paramRid = request.getOptionalPathParameter("rid");
        Table table;
        if (paramRid.isPresent()) {
            table = new Table("RID", "Name", "Location", "Capacity", "Description", "Labels");
            int rid = paramRid.get().toInt();
            Room room = provider.execute(conn ->
                    new RoomQueries(conn).getRoom(rid));

            Iterable<Label> labels = provider.execute(conn ->
                    new RoomLabelQueries(conn).getRoomLabels(rid));

            table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                    String.valueOf(room.getCapacity()), room.getDescription(), labels.toString());
        } else {
            Optional<List<Parameter>> paramBegin = request.getOptionalParameter("begin");
            Optional<List<Parameter>> paramDuration = request.getOptionalParameter("duration");
            Optional<List<Parameter>> paramCapacity = request.getOptionalParameter("capacity");
            Optional<List<Parameter>> paramLabel = request.getOptionalParameter("label");

            table = new Table("RID", "Name", "Location", "Capacity", "Description");
            Iterable<Room> rooms = null;
            if (paramBegin.isPresent() || paramDuration.isPresent()
                    || paramCapacity.isPresent() || paramLabel.isPresent()) {
                rooms = provider.execute(conn ->
                        new RoomQueries(conn).getRooms(paramBegin, paramDuration, paramCapacity, paramLabel));
            } else {
                rooms = provider.execute(conn ->
                        new RoomQueries(conn).getRooms());
            }

            for (Room room : rooms) {
                table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                        String.valueOf(room.getCapacity()), room.getDescription());
            }
        }

        return new HandlerResponse(new TableView(table));
    }
}
