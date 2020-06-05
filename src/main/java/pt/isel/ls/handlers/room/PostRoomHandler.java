package pt.isel.ls.handlers.room;

import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.validator.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Validator;
import pt.isel.ls.router.request.validator.ValidatorResult;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.misc.IdentifierView;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class PostRoomHandler extends RouteHandler {

    public PostRoomHandler(ConnectionProvider provider) {
        super(
                Method.POST,
                "/rooms",
                "Creates a new room",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        ValidatorResult res = getValidator().validate(request);
        if (res.hasErrors()) {
            throw new ValidatorException(res);
        }

        String name = res.getParameterValue("name");
        int capacity = res.getParameterValue("capacity");
        String location = res.getParameterValue("location");
        Optional<List<String>> optLabels = res.getOptionalParameter("label");
        Optional<String> desc = res.getOptionalParameter("description");

        Room inserted = createRoom(name, capacity, location, desc.orElse(null), optLabels.orElse(null));
        return new HandlerResponse(new IdentifierView("room", inserted.getRid()));
    }

    Room createRoom(String name, int capacity, String location, String desc, List<String> optLabels) {
        return provider.execute(handler -> {
            List<Label> labels = new LinkedList<>();
            if (optLabels != null) {
                LabelQueries labelQueries = new LabelQueries(handler);
                for (String label : optLabels) {
                    labels.add(labelQueries.getLabel(label));
                }
            }

            return new RoomQueries(handler).createNewRoom(name, location, capacity, desc, labels);
        });
    }

    Validator getValidator() {
        return new Validator()
                .addMapping("name", p -> p.getUnique().toString())
                .addFilter("name", name -> provider.execute(handler -> {
                    new RoomQueries(handler).checkNameAvailability(name);
                    return null;
                }), String.class)
                .addMapping("capacity", p -> p.getUnique().toInt())
                .addMapping("location", p -> p.getUnique().toString())
                .addMapping("label", p -> p.map(Parameter::toString), true)
                .addMapping("description", p -> p.getUnique().toString(), true);
    }

}
