package pt.isel.ls.handlers;

import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteResponse;

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
    public RouteResponse execute(RouteRequest request) throws RouteException {
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
            table = new Table("RID", "Name", "Location", "Capacity", "Description");
            Iterable<Room> rooms = provider.execute(conn ->
                    new RoomQueries(conn).getRooms());

            for (Room room : rooms) {
                table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                        String.valueOf(room.getCapacity()), room.getDescription());
            }
        }

        return new RouteResponse(new TableView(table));
    }
}
