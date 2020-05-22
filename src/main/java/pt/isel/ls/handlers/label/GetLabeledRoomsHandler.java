package pt.isel.ls.handlers.label;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.view.label.LabeledRoomsView;

import java.util.stream.Collectors;

public final class GetLabeledRoomsHandler extends RouteHandler {

    public GetLabeledRoomsHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/labels/{lid}/rooms",
                "Gets all of the rooms with a certain label",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        int lid = request.getPathParameter("lid").toInt();
        Iterable<Room> rooms = provider.execute(handler -> new RoomLabelQueries(handler)
                .getLabeledRooms(lid))
                .collect(Collectors.toList());

        return new HandlerResponse(new LabeledRoomsView(lid, rooms));
    }

}
