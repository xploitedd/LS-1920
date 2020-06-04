package pt.isel.ls.handlers.booking;

import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.parameter.Validator;
import pt.isel.ls.router.request.parameter.ValidatorResult;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.utils.Time;
import pt.isel.ls.view.misc.IdentifierView;

import java.sql.Timestamp;

public final class PostBookingHandler extends RouteHandler {

    public PostBookingHandler(ConnectionProvider provider) {
        super(
                Method.POST,
                "/rooms/{rid}/bookings",
                "Creates a new booking",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        ValidatorResult res = getValidator().validate(request);
        if (res.hasErrors()) {
            throw new ValidatorException(res);
        }

        int uid = res.getParameterValue("uid");
        long b = res.getParameterValue("begin");
        int duration = res.getParameterValue("duration");

        Booking booking = createBooking(rid, uid, b, duration);
        return new HandlerResponse(new IdentifierView("booking", booking.getBid()));
    }

    Booking createBooking(int rid, int uid, long begin, int duration) {
        Timestamp b = new Timestamp(begin);
        // duration in minutes
        Timestamp e = new Timestamp(begin + Time.minutesToMillis(duration));

        return provider.execute(handler ->
                new BookingQueries(handler).createNewBooking(rid, uid, b, e));
    }

    Validator getValidator() {
        return new Validator()
                .addRule("uid", p -> p.getUnique().toInt())
                .addRule("begin", p -> p.getUnique().toTime())
                .addRule("duration", p -> p.getUnique().toInt());
    }

}
