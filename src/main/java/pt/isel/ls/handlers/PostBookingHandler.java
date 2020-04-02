package pt.isel.ls.handlers;

import pt.isel.ls.model.Booking;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteResponse;
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
    public RouteResponse execute(RouteRequest request) throws RouteException {
        int rid = request.getPathParameter("rid").toInt();
        int uid = request.getParameter("uid").get(0).toInt();
        long begin = request.getParameter("begin").get(0).toLong();
        long end = request.getParameter("end").get(0).toLong();
        Timestamp b = new Timestamp(begin);
        Timestamp e = new Timestamp(end);

        Booking booking = provider.execute(conn ->
                new BookingQueries(conn).createNewBooking(rid, uid, b, e));

        return new RouteResponse(new IdentifierView("booking", booking.getBid()));
    }
}
