package pt.isel.ls.handlers;

import pt.isel.ls.model.Label;
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

    private static final String DESCRIPTION = "Gets all of the Rooms or a specific room";

    private final ConnectionProvider provider;

    public GetRoomsHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets all of the Rooms or a specific room
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        Optional<Parameter> paramRid = request.getOptionalPathParameter("rid");
        Table table;
        if (paramRid.isPresent()) {
            table = new Table("RID", "Name", "Location", "Capacity", "Description", "Labels");
            int rid = paramRid.get().toInt();
            Room room = provider.execute(conn ->
                    new RoomQueries(conn).getRoom(rid));

            Iterable<Label> labels = provider.execute(conn -> new RoomLabelQueries(conn)
                    .getRoomLabels(rid))
                    .collect(Collectors.toList());

            table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                    String.valueOf(room.getCapacity()), room.getDescription(), labels.toString());
        } else {
            Optional<List<Parameter>> paramBegin = request.getOptionalParameter("begin");
            Optional<List<Parameter>> paramDuration = request.getOptionalParameter("duration");
            Optional<List<Parameter>> paramCapacity = request.getOptionalParameter("capacity");
            Optional<List<Parameter>> paramLabel = request.getOptionalParameter("label");

            table = new Table("RID", "Name", "Location", "Capacity", "Description");
            Iterable<Room> rooms = provider.execute(conn -> {
                Stream<Room> roomStream = new RoomQueries(conn).getRooms();
                if (paramCapacity.isPresent()) {
                    int capacity = paramCapacity.get().get(0).toInt();
                    roomStream = roomStream.filter(room -> room.getCapacity() >= capacity);
                }

                if (paramLabel.isPresent()) {
                    RoomLabelQueries labelQueries = new RoomLabelQueries(conn);
                    for (Parameter labelP : paramLabel.get()) {
                        int lid = labelP.toInt();
                        roomStream = roomStream.filter(room -> ExceptionUtils.propagate(() ->
                                labelQueries.isLabelInRoom(room.getRid(), lid)
                        ));
                    }
                }

                if (paramBegin.isPresent() && paramDuration.isPresent()) {
                    long begin = paramBegin.get().get(0).toLong();
                    int duration = paramDuration.get().get(0).toInt();
                    Interval i1 = new Interval(begin, begin + duration);

                    BookingQueries bookingQueries = new BookingQueries(conn);
                    roomStream = roomStream.filter(room -> ExceptionUtils.propagate(() -> {
                        Iterable<Interval> bookingInt = bookingQueries.getBookingsByRid(room.getRid())
                                .map(b -> new Interval(b.getBegin().getTime(), b.getEnd().getTime()))
                                .collect(Collectors.toList());

                        for (Interval interval : bookingInt) {
                            if (i1.isOverlapping(interval)) {
                                return false;
                            }
                        }

                        return true;
                    }));
                }

                return roomStream.collect(Collectors.toList());
            });

            for (Room room : rooms) {
                table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                        String.valueOf(room.getCapacity()), room.getDescription());
            }
        }

        return new HandlerResponse(new TableView(table));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

}
