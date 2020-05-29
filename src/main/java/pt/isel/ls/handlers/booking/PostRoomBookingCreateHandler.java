package pt.isel.ls.handlers.booking;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;

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
        try {
            Booking newBooking = new PostBookingHandler(provider).createBooking(request);
            return new HandlerResponse()
                    .redirect(GetRoomBookingHandler.class, rid, newBooking.getBid());
        } catch (AppException e) {
            return new HandlerResponse()
                    .redirect(GetRoomBookingCreateHandler.class, rid)
                    .getRedirect()
                    .setError(e.getMessage())
                    .getResponse();
        }
    }
}
