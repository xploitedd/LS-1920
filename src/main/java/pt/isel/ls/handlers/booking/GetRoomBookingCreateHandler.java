package pt.isel.ls.handlers.booking;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.booking.RoomBookingCreateView;

public class GetRoomBookingCreateHandler extends RouteHandler {

    public GetRoomBookingCreateHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/rooms/{rid}/bookings/create",
                "Return a html page with a form to create a booking",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        Room room = provider.execute(handler -> new RoomQueries(handler).getRoom(rid));

        return new HandlerResponse(new RoomBookingCreateView(room));
    }
}
