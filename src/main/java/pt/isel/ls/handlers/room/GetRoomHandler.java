package pt.isel.ls.handlers.room;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.TableView;

import java.util.stream.Collectors;

public final class GetRoomHandler extends RouteHandler {

    public GetRoomHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/rooms/{rid}",
                "Get a specific room",
                provider
        );
    }

    /**
     * Get a specific room
     * @param request The request to be executed
     * @return a new HandlerResponse
     * @throws RouteException any exception that occurred
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        Parameter paramRid = request.getPathParameter("rid");
        Table table = new Table("RID", "Name", "Location", "Capacity", "Description", "Labels");
        int rid = paramRid.toInt();
        Room room = provider.execute(conn ->
                new RoomQueries(conn).getRoom(rid));

        Iterable<Label> labels = provider.execute(handler -> new RoomLabelQueries(handler)
                .getRoomLabels(rid))
                .collect(Collectors.toList());

        table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                String.valueOf(room.getCapacity()), room.getDescription(), labels.toString());

        return new HandlerResponse(new TableView("Room " + rid + " details", table));
    }

}
