package pt.isel.ls.handlers.room;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.utils.ExceptionUtils;
import pt.isel.ls.utils.Interval;
import pt.isel.ls.view.TableView;

public final class GetRoomsHandler implements RouteHandler {

    private static final String DESCRIPTION = "Gets all of the Rooms";

    private final ConnectionProvider provider;

    public GetRoomsHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets all of the Rooms
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        Optional<List<Parameter>> paramBegin = request.getOptionalParameter("begin");
        Optional<List<Parameter>> paramDuration = request.getOptionalParameter("duration");
        Optional<List<Parameter>> paramCapacity = request.getOptionalParameter("capacity");
        Optional<List<Parameter>> paramLabel = request.getOptionalParameter("label");

        Table table = new Table("RID", "Name", "Location", "Capacity", "Description");
        Stream<Room> rooms = provider.execute(conn -> {
            Stream<Room> ret = new RoomQueries(conn).getRooms();
            if (paramCapacity.isPresent()) {
                int minCapacity = paramCapacity.get().get(0).toInt();
                ret = ret.filter(room -> room.getCapacity() >= minCapacity);
            }

            return ret;
        });

        if (paramLabel.isPresent()) {
            for (Parameter labelPar : paramLabel.get()) {
                rooms = rooms.filter(room -> ExceptionUtils.propagate(() -> provider.execute(conn ->
                        new RoomLabelQueries(conn).isLabelInRoom(room.getRid(), labelPar.toString()))));
            }
        }

        if (paramBegin.isPresent() && paramDuration.isPresent()) {
            long begin = paramBegin.get().get(0).toLong();
            long end = begin + 60000 * paramDuration.get().get(0).toInt();
            Interval i = new Interval(begin, end);
            rooms = rooms.filter(room -> ExceptionUtils.propagate(() -> provider.execute(conn -> {
                    Iterable<Booking> bookings = new BookingQueries(conn)
                            .getBookingsByRid(room.getRid())
                            .collect(Collectors.toList());

                    for (Booking b : bookings) {
                        Interval bi = new Interval(b.getBegin().getTime(), b.getEnd().getTime());
                        if (i.isOverlapping(bi)) {
                            return false;
                        }
                    }

                    return true;
                })
            ));
        }

        rooms.forEach(room ->
                table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                        String.valueOf(room.getCapacity()), room.getDescription()));

        return new HandlerResponse(new TableView("Rooms", table));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

}
