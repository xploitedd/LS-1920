package pt.isel.ls.handlers.room;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.utils.Interval;
import pt.isel.ls.utils.Time;
import pt.isel.ls.view.TableView;

public final class GetRoomsHandler extends RouteHandler {

    public GetRoomsHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/rooms",
                "Gets all of the Rooms",
                provider
        );
    }

    /**
     * Gets all of the Rooms
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        Optional<List<Parameter>> paramBegin = request.getOptionalParameter("begin");
        Optional<List<Parameter>> paramDuration = request.getOptionalParameter("duration");
        Optional<List<Parameter>> paramCapacity = request.getOptionalParameter("capacity");
        Optional<List<Parameter>> paramLabel = request.getOptionalParameter("label");

        Table table = new Table("RID", "Name", "Location", "Capacity", "Description");
        Iterable<Room> rooms = provider.execute(handler -> {
            Stream<Room> ret = new RoomQueries(handler).getRooms();
            if (paramCapacity.isPresent()) {
                int minCapacity = paramCapacity.get().get(0).toInt();
                ret = ret.filter(room -> room.getCapacity() >= minCapacity);
            }

            if (paramLabel.isPresent()) {
                RoomLabelQueries rlq = new RoomLabelQueries(handler);
                for (Parameter labelPar : paramLabel.get()) {
                    ret = ret.filter(room ->
                            rlq.isLabelInRoom(room.getRid(), labelPar.toString()));
                }
            }

            if (paramBegin.isPresent() && paramDuration.isPresent()) {
                long begin = paramBegin.get().get(0).toLong();
                long end = begin + Time.minutesToMillis(paramDuration.get().get(0).toLong());
                Interval i = new Interval(begin, end);
                ret = ret.filter(room -> {
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

            // collect the results due to the queries being made on filter operations
            return ret.collect(Collectors.toList());
        });

        rooms.forEach(room ->
                table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                        String.valueOf(room.getCapacity()), room.getDescription()));

        return new HandlerResponse(new TableView("Rooms", table));
    }

}
