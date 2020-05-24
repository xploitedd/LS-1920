package pt.isel.ls.handlers.label;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.view.label.LabelView;

import java.util.stream.Stream;

public class GetLabelHandler extends RouteHandler {

    public GetLabelHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/labels/{lid}",
                "Get details of a specific label",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        int lid = request.getPathParameter("lid").toInt();
        Label label = provider.execute(handler -> new LabelQueries(handler).getLabelById(lid));
        Stream<Room> rooms = provider.execute(handler -> new RoomLabelQueries(handler)
                    .getLabeledRooms(lid));

        return new HandlerResponse(new LabelView(label, rooms));
    }

}
