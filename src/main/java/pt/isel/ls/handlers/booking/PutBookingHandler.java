package pt.isel.ls.handlers.booking;

import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.ValidatorResult;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.utils.Time;
import pt.isel.ls.view.misc.IdentifierView;

import java.sql.Timestamp;

public final class PutBookingHandler extends RouteHandler {

    public PutBookingHandler(ConnectionProvider provider) {
        super(
                Method.PUT,
                "/rooms/{rid}/bookings/{bid}",
                "Updates a booking",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        int bid = request.getPathParameter("bid").toInt();

        PostBookingHandler pbh = new PostBookingHandler(provider);
        ValidatorResult res = pbh.getValidator().validate(request);
        if (res.hasErrors()) {
            throw new ValidatorException(res);
        }

        int newUid = res.getParameterValue("uid");
        long newBegin = res.getParameterValue("begin");
        int duration = res.getParameterValue("duration");

        Timestamp newB = new Timestamp(newBegin);
        Timestamp newE = new Timestamp(newBegin + Time.minutesToMillis(duration));

        Booking booking = provider.execute(handler -> new BookingQueries(handler)
                .editBooking(rid, bid, newUid, newB, newE));

        return new HandlerResponse(new IdentifierView("updated", "booking", booking.getBid()));
    }

}
