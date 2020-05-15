package pt.isel.ls.handlers.room;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.handlers.booking.GetRoomBookingsHandler;
import pt.isel.ls.handlers.label.GetLabelHandler;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.Table;
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
        Table table = new Table(
                "RID",
                "Name",
                "Location",
                "Capacity",
                "Description",
                "Labels",
                "Bookings"
        );

        int rid = paramRid.toInt();
        provider.execute(handler -> {
            Room room = new RoomQueries(handler).getRoom(rid);
            Iterable<String> labels = new RoomLabelQueries(handler)
                    .getRoomLabels(rid)
                    .map(l -> router.routeFromName(GetLabelHandler.class, l.getLid()))
                    .collect(Collectors.toList());

            String bookings = router.routeFromName(GetRoomBookingsHandler.class, rid);
            table.addTableRow(room.getRid(), room.getName(), room.getLocation(),
                    room.getCapacity(), room.getDescription(), labels, bookings);

            return null;
        });

        return new HandlerResponse(new RoomView("Room " + rid + " details", table));
    }

}
