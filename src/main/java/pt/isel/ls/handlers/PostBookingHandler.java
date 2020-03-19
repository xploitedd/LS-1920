package pt.isel.ls.handlers;

import pt.isel.ls.model.Booking;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;
import java.sql.Timestamp;

public class PostBookingHandler implements RouteHandler {

    private DataSource dataSource;

    public PostBookingHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        Booking booking = new ConnectionProvider(dataSource).execute(conn -> {
            int rid = Integer.parseInt(request.getPathParameter("rid"));
            int uid = Integer.parseInt(request.getParameter("uid").get(0));
            String begin = request.getParameter("begin").get(0);
            String end = request.getParameter("end").get(0);
            //TODO: Make sure these Strings are TIMESTAMPs
            Timestamp b = Timestamp.valueOf(begin);
            Timestamp e = Timestamp.valueOf(end);
            return new BookingQueries(conn).createNewBooking(rid,uid,b,e);
        });

        return new RouteResponse(new IdentifierView("booking", booking.getBid()));
    }
}
