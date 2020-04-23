package pt.isel.ls.handlers.room;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        provider.execute(conn -> {
            Stream<Room> roomStream = new RoomQueries(conn).getRooms();
            if (paramCapacity.isPresent()) {
                int capacity = paramCapacity.get().get(0).toInt();
                roomStream = filterByCapacity(roomStream, capacity);
            }

            if (paramLabel.isPresent()) {
                RoomLabelQueries labelQueries = new RoomLabelQueries(conn);
                IntStream lidStream = paramLabel.get()
                        .stream()
                        .mapToInt(lid -> ExceptionUtils.propagate(lid::toInt));

                roomStream = filterByLabels(roomStream, lidStream, labelQueries);
            }

            if (paramBegin.isPresent() && paramDuration.isPresent()) {
                long begin = paramBegin.get().get(0).toLong();
                int duration = paramDuration.get().get(0).toInt();
                Interval i1 = new Interval(begin, begin + duration);

                BookingQueries bookingQueries = new BookingQueries(conn);
                roomStream = filterByAvailability(roomStream, i1, bookingQueries);
            }

            return roomStream;
        }).forEach(room -> {
            table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                    String.valueOf(room.getCapacity()), room.getDescription());
        });

        return new HandlerResponse(new TableView("Rooms", table));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    private Stream<Room> filterByCapacity(Stream<Room> roomStream, int minCapacity) {
        return roomStream.filter(room -> room.getCapacity() >= minCapacity);
    }

    private Stream<Room> filterByLabels(Stream<Room> roomStream,
                                        IntStream labelIds,
                                        RoomLabelQueries labelQueries) {

        for (int lid : labelIds.toArray()) {
            roomStream = roomStream.filter(room -> ExceptionUtils.propagate(() ->
                    labelQueries.isLabelInRoom(room.getRid(), lid)
            ));
        }

        return roomStream;
    }

    private Stream<Room> filterByAvailability(Stream<Room> roomStream,
                                              Interval available,
                                              BookingQueries queries) {

        return roomStream.filter(room -> ExceptionUtils.propagate(() -> {
            Iterable<Interval> bookingInt = queries.getBookingsByRid(room.getRid())
                    .map(b -> new Interval(b.getBegin().getTime(), b.getEnd().getTime()))
                    .collect(Collectors.toList());

            for (Interval interval : bookingInt) {
                if (available.isOverlapping(interval)) {
                    return false;
                }
            }

            return true;
        }));
    }

}
