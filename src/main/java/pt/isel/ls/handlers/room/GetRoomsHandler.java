package pt.isel.ls.handlers.room;

import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.parameter.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.parameter.Validator;
import pt.isel.ls.router.request.parameter.ValidatorResult;
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
import java.util.Set;
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
        Validator validator = new Validator()
                .addRule("begin", p -> p.getUnique().toTime(), true)
                .addRule("duration", p -> p.getUnique().toInt(), true)
                .addRule("capacity", p -> p.getUnique().toInt(), true)
                .addRule("label", p -> p.map(Parameter::toString), true);

        ValidatorResult res = validator.validate(request);
        if (res.hasErrors()) {
            throw new ValidatorException(res);
        }

        Optional<Long> paramBegin = res.getOptionalParameter("begin");
        Optional<Integer> paramDuration = res.getOptionalParameter("duration");
        Optional<Integer> paramCapacity = res.getOptionalParameter("capacity");
        Optional<List<String>> paramLabel = res.getOptionalParameter("label");

        Set<Room> rooms = provider.execute(handler -> {
            Stream<Room> str = new RoomQueries(handler).getRooms();
            if (paramCapacity.isPresent()) {
                int minCapacity = paramCapacity.get();
                str = str.filter(room -> room.getCapacity() >= minCapacity);
            }

            Set<Room> ret = str.collect(Collectors.toSet());
            if (paramLabel.isPresent()) {
                ret = filterByLabels(handler, ret, paramLabel.get());
            }

            if (paramBegin.isPresent() && paramDuration.isPresent()) {
                long begin = paramBegin.get();
                long end = begin + Time.minutesToMillis(paramDuration.get());
                ret = filterByTime(handler, ret, begin, end);
            }

            return ret;
        });

        return new HandlerResponse(new RoomsView(rooms));
    }

    /**
     * Filter the specified stream by elements that have all of the
     * specified labels
     * @param handler SQL Handler
     * @param ret Set to be filtered
     * @param labels Labels to filter for
     * @return The filtered set
     */
    private static Set<Room> filterByLabels(SqlHandler handler, final Set<Room> ret, List<String> labels) {
        RoomLabelQueries rlq = new RoomLabelQueries(handler);
        return rlq.getRoomsWithLabels(labels)
                .filter(ret::contains)
                .collect(Collectors.toSet());
    }

    /**
     * Filter the specified stream by elements that are available
     * in the specified time slot
     * @param handler SQL Handler
     * @param ret Set to be filtered
     * @param begin begin instant of the time slot
     * @param end end instant of the time slot
     * @return The filtered set
     */
    private static Set<Room> filterByTime(SqlHandler handler, final Set<Room> ret, long begin, long end) {
        Interval i = new Interval(begin, end);
        return new BookingQueries(handler)
                .getRoomsAvailable(i)
                .filter(ret::contains)
                .collect(Collectors.toSet());
    }

}
