package pt.isel.ls.handlers.booking;

import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.handlers.validators.CreateBookingValidator;
import pt.isel.ls.model.Booking;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
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
        CreateBookingValidator validator = new CreateBookingValidator(request);
        if (validator.hasErrors()) {
            throw new ValidatorException(validator.getResult());
        }

        int uid = validator.getUserId();
        long b = validator.getBegin();
        int duration = validator.getDuration();

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

}
