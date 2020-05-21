package pt.isel.ls.handlers.room;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.room.RoomSearchView;

import java.util.stream.Collectors;

public class GetRoomSearchHandler extends RouteHandler {

    public GetRoomSearchHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/rooms/search",
                "Search a room",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        Iterable<Label> availableLabels = provider.execute(handler -> new LabelQueries(handler)
                .getLabels()
                .collect(Collectors.toList()));

        return new HandlerResponse(new RoomSearchView(availableLabels));
    }

}
