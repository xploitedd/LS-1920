package pt.isel.ls.handlers;

import pt.isel.ls.model.Booking;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.console.TableView;

import javax.sql.DataSource;

public final class GetUserBookingsHandler implements RouteHandler {

    private final DataSource dataSource;

    public GetUserBookingsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets bookings booked by a user
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public RouteResponse execute(RouteRequest request) throws RouteException {
        int uid = request.getPathParameter("uid").toInt();
        Iterable<Booking> iter = new ConnectionProvider(dataSource)
                .execute(conn -> new BookingQueries(conn).getBookingsByUid(uid));

        Table table = new Table("Booking Id", "Room Id", "Begin time", "End time");
        for (Booking booking : iter) {
            table.addTableRow(String.valueOf(booking.getBid()), String.valueOf(booking.getRid()),
                    booking.getBegin().toString(), booking.getEnd().toString());
        }

        return new RouteResponse(new TableView(table));

    }
}
