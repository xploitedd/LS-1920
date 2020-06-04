package pt.isel.ls.handlers.booking;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.User;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.booking.RoomBookingCreateView;

public class PostRoomBookingCreateHandler extends RouteHandler {

    public PostRoomBookingCreateHandler(ConnectionProvider provider) {
        super(
                Method.POST,
                "/rooms/{rid}/bookings/create",
                "Creates a new Booking",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        String email = request.getParameter("email").get(0).toString();
        long begin = request.getParameter("begin").get(0).toTime();
        int duration = request.getParameter("duration").get(0).toInt();

        try {
            User user = provider.execute(handler -> new UserQueries(handler)
                    .getUser(email));

            Booking newBooking = new PostBookingHandler(provider)
                    .createBooking(rid, user.getUid(), begin, duration);

            return new HandlerResponse()
                    .redirect(GetRoomBookingHandler.class, rid, newBooking.getBid());
        } catch (AppException e) {
            Room room = provider.execute(handler -> new RoomQueries(handler).getRoom(rid));
            return new HandlerResponse(new RoomBookingCreateView(room, e.getMessage()))
                    .setStatusCode(e.getStatusCode());
        }
    }
}
