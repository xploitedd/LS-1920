package pt.isel.ls.handlers.validators;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Parameter;
import pt.isel.ls.router.request.validator.Validator;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomQueries;

import java.util.List;
import java.util.Optional;

public class CreateRoomValidator extends AbstractValidator {

    private static final int ROOM_NAME_MAX_LENGTH = 50;
    private static final int ROOM_MIN_CAPACITY = 2;
    private static final int ROOM_LOCATION_MAX_LENGTH = 50;
    private static final int ROOM_DESCRIPTION_MAX_LENGTH = 200;

    public CreateRoomValidator(RouteRequest request, ConnectionProvider provider) {
        super(new Validator(request)
                .addMapping("name", p -> p.getUnique().toString())
                .addFilter("name", s -> s.length() <= ROOM_NAME_MAX_LENGTH,
                        String.class, "Name max length is " + ROOM_NAME_MAX_LENGTH)
                .addFilter("name", name -> provider.execute(handler -> {
                    new RoomQueries(handler).checkNameAvailability(name);
                    return true;
                }), String.class)
                .addMapping("capacity", p -> p.getUnique().toInt())
                .addFilter("capacity", capacity -> capacity >= ROOM_MIN_CAPACITY,
                        Integer.class, "Room capacity should be at least " + ROOM_MIN_CAPACITY)
                .addMapping("location", p -> p.getUnique().toString())
                .addFilter("location", loc -> loc.length() <= ROOM_LOCATION_MAX_LENGTH,
                        String.class, "Location max length is " + ROOM_LOCATION_MAX_LENGTH)
                .addMapping("label", p -> p.map(Parameter::toString), true)
                .addMapping("description", p -> p.getUnique().toString(), true)
                .addFilter("description", desc -> desc.length() <= ROOM_DESCRIPTION_MAX_LENGTH,
                        String.class, "Description max length is " + ROOM_LOCATION_MAX_LENGTH));
    }

    public String getName() {
        return getParameter("name");
    }

    public int getCapacity() {
        return getParameter("capacity");
    }

    public String getLocation() {
        return getParameter("location");
    }

    public Optional<List<String>> getLabels() {
        return getOptionalParameter("label");
    }

    public Optional<String> getDescription() {
        return getOptionalParameter("description");
    }

}
