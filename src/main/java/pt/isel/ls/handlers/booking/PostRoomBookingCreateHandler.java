package pt.isel.ls.handlers.booking;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.User;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Validator;
import pt.isel.ls.router.request.validator.ValidatorResult;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.error.HandlerError;
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
        Validator validator = new Validator()
                .addMapping("email", p -> provider.execute(handler -> new UserQueries(handler)
                        .getUserByEmail(p.getUnique().toString())))
                .addMapping("begin", p -> p.getUnique().toTime())
                .addMapping("duration", p -> p.getUnique().toInt());

        ValidatorResult res = validator.validate(request);
        if (res.hasErrors()) {
            return new HandlerResponse(new RoomBookingCreateView(getRoom(rid), res.getErrors(), request))
                    .setStatusCode(StatusCode.BAD_REQUEST);
        }

        User user = res.getParameterValue("email");
        long begin = res.getParameterValue("begin");
        int duration = res.getParameterValue("duration");

        try {
            Booking newBooking = new PostBookingHandler(provider)
                    .createBooking(rid, user.getUid(), begin, duration);

            return new HandlerResponse()
                    .redirect(GetRoomBookingHandler.class, rid, newBooking.getBid());
        } catch (AppException e) {
            HandlerError err = HandlerError.fromException(e);
            return new HandlerResponse(new RoomBookingCreateView(getRoom(rid), err))
                    .setStatusCode(e.getStatusCode());
        }
    }

    private Room getRoom(int rid) {
        return provider.execute(handler -> new RoomQueries(handler).getRoom(rid));
    }

}
