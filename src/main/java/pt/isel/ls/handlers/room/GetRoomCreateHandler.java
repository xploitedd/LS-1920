package pt.isel.ls.handlers.room;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.room.RoomCreateView;

import java.util.stream.Collectors;

public class GetRoomCreateHandler extends RouteHandler {

    public GetRoomCreateHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/rooms/create",
                "Return a html page with a form to create a room",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        Iterable<Label> availableLabels = provider.execute(handler -> new LabelQueries(handler)
                .getLabels()
                .collect(Collectors.toList()));

        return new HandlerResponse(new RoomCreateView(availableLabels));
    }
}
