package pt.isel.ls.handlers;

import pt.isel.ls.model.Booking;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.IdentifierView;

import java.sql.Timestamp;

public final class PostBookingHandler implements RouteHandler {

    private final ConnectionProvider provider;

    public PostBookingHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        int rid = request.getPathParameter("rid").toInt();
        int uid = request.getParameter("uid").get(0).toInt();
        long b = request.getParameter("begin").get(0).toLong();
        long duration = request.getParameter("duration").get(0).toLong();

        Timestamp begin = new Timestamp(b);
        Timestamp end = new Timestamp(b + duration);

        Booking booking = provider.execute(conn ->
                new BookingQueries(conn).createNewBooking(rid, uid, begin, end));

        return new HandlerResponse(new IdentifierView("booking", booking.getBid()));
    }
}
