package pt.isel.ls.handlers.label;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.view.label.LabelView;

import java.util.stream.Collectors;

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
    public HandlerResponse execute(Router router, RouteRequest request) {
        int lid = request.getPathParameter("lid").toInt();
        Table table = new Table("Id", "Name", "Rooms");
        provider.execute(handler -> {
            Label label = new LabelQueries(handler).getLabelById(lid);
            Iterable<String> rooms = new RoomLabelQueries(handler)
                    .getLabeledRooms(lid)
                    .map(room -> router.routeFromName(GetRoomHandler.class, room.getRid()))
                    .collect(Collectors.toList());

            table.addTableRow(label.getLid(), label.getName(), rooms);
            return null;
        });

        return new HandlerResponse(new LabelView("Label " + lid, table));
    }

}
