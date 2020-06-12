package pt.isel.ls.handlers.room;

import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.handlers.validators.CreateRoomValidator;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
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
        CreateRoomValidator validator = new CreateRoomValidator(request, provider);
        if (validator.hasErrors()) {
            throw new ValidatorException(validator.getResult());
        }

        String name = validator.getName();
        int capacity = validator.getCapacity();
        String location = validator.getLocation();
        Optional<List<String>> optLabels = validator.getLabels();
        Optional<String> desc = validator.getDescription();

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

}
