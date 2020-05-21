package pt.isel.ls.handlers.room;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.room.RoomView;

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
    public HandlerResponse execute(Router router, RouteRequest request) {
        Parameter paramRid = request.getPathParameter("rid");

        int rid = paramRid.toInt();
        Room room = provider.execute(handler -> new RoomQueries(handler).getRoom(rid));
        Iterable<Label> labels = provider.execute(handler -> new RoomLabelQueries(handler)
                .getRoomLabels(rid)
                .collect(Collectors.toList()));

        return new HandlerResponse(new RoomView("Room " + rid + " details", room, labels));
    }

}
