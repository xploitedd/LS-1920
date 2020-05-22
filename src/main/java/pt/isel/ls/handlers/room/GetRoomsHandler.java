package pt.isel.ls.handlers.room;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.api.SqlHandler;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.utils.Interval;
import pt.isel.ls.utils.Time;
import pt.isel.ls.view.room.RoomsView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GetRoomsHandler extends RouteHandler {

    public GetRoomsHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/rooms",
                "Gets all of the Rooms",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        Optional<List<Parameter>> paramBegin = request.getOptionalParameter("begin");
        Optional<List<Parameter>> paramDuration = request.getOptionalParameter("duration");
        Optional<List<Parameter>> paramCapacity = request.getOptionalParameter("capacity");
        Optional<List<Parameter>> paramLabel = request.getOptionalParameter("label");

        Iterable<Room> rooms = provider.execute(handler -> {
            Stream<Room> ret = new RoomQueries(handler).getRooms();
            if (paramCapacity.isPresent()) {
                int minCapacity = paramCapacity.get().get(0).toInt();
                ret = ret.filter(room -> room.getCapacity() >= minCapacity);
            }

            if (paramLabel.isPresent()) {
                ret = filterByLabels(handler, ret, paramLabel.get());
            }

            if (paramBegin.isPresent() && paramDuration.isPresent()) {
                long begin = paramBegin.get().get(0).toTime();
                long end = begin + Time.minutesToMillis(paramDuration.get().get(0).toTime());
                ret = filterByTime(handler, ret, begin, end);
            }

            // collect the results due to the queries being made on filter operations
            return ret.collect(Collectors.toList());
        });

        return new HandlerResponse(new RoomsView(rooms));
    }

    /**
     * Filter the specified stream by elements that have all of the
     * specified labels
     * @param handler SQL Handler
     * @param ret Stream to be filtered
     * @param labels Labels to filter for
     * @return The filtered stream
     */
    private static Stream<Room> filterByLabels(SqlHandler handler, Stream<Room> ret, List<Parameter> labels) {
        RoomLabelQueries rlq = new RoomLabelQueries(handler);
        for (Parameter labelPar : labels) {
            ret = ret.filter(room ->
                    rlq.isLabelInRoom(room.getRid(), labelPar.toString()));
        }

        return ret;
    }

    /**
     * Filter the specified stream by elements that are available
     * in the specified time slot
     * @param handler SQL Handler
     * @param ret Stream to be filtered
     * @param begin begin instant of the time slot
     * @param end end instant of the time slot
     * @return The filtered stream
     */
    private static Stream<Room> filterByTime(SqlHandler handler, Stream<Room> ret, long begin, long end) {
        Interval i = new Interval(begin, end);
        return ret.filter(room -> {
            Iterable<Booking> bookings = new BookingQueries(handler)
                    .getBookingsByRid(room.getRid())
                    .collect(Collectors.toList());

            for (Booking b : bookings) {
                Interval bi = new Interval(b.getBegin().getTime(), b.getEnd().getTime());
                if (i.isOverlapping(bi)) {
                    return false;
                }
            }

            return true;
        });
    }

}
