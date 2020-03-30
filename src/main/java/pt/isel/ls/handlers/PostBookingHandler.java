package pt.isel.ls.handlers;

import pt.isel.ls.model.Booking;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;
import java.sql.Timestamp;

public final class PostBookingHandler implements RouteHandler {

    private final DataSource dataSource;

    public PostBookingHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws RouteException {
        int rid = request.getPathParameter("rid").toInt();
        int uid = request.getParameter("uid").get(0).toInt();
        long begin = request.getParameter("begin").get(0).toLong();
        long end = request.getParameter("end").get(0).toLong();
        Timestamp b = new Timestamp(begin);
        Timestamp e = new Timestamp(end);

        Booking booking = new ConnectionProvider(dataSource)
                .execute(conn -> new BookingQueries(conn).createNewBooking(rid,uid,b,e));

        return new RouteResponse(new IdentifierView("booking", booking.getBid()));
    }
}
