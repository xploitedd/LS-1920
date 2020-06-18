package pt.isel.ls.handlers.room;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.handlers.validators.CreateRoomValidator;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.error.HandlerErrors;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.room.RoomCreateView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostRoomCreateHandler extends RouteHandler {

    public PostRoomCreateHandler(ConnectionProvider provider) {
        super(
                Method.POST,
                "/rooms/create",
                "Creates a new Room",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        CreateRoomValidator validator = new CreateRoomValidator(request, provider);
        if (validator.hasErrors()) {
            return new HandlerResponse(
                    new RoomCreateView(getAvailableLabels(), validator.getResult().getErrors())
            ).setStatusCode(StatusCode.BAD_REQUEST);
        }

        String name = validator.getName();
        int capacity = validator.getCapacity();
        String location = validator.getLocation();
        Optional<List<String>> optLabels = validator.getLabels();
        Optional<String> desc = validator.getDescription();

        try {
            Room newRoom = new PostRoomHandler(provider)
                    .createRoom(name, capacity, location, desc.orElse(null), optLabels.orElse(null));

            return new HandlerResponse()
                    .redirect(GetRoomHandler.class, newRoom.getRid());
        } catch (AppException e) {
            HandlerErrors err = HandlerErrors.fromException(e);
            return new HandlerResponse(new RoomCreateView(getAvailableLabels(), err))
                    .setStatusCode(e.getStatusCode());
        }
    }

    private Iterable<Label> getAvailableLabels() {
        return provider.execute(handler -> new LabelQueries(handler)
                .getLabels()
                .collect(Collectors.toList()));
    }

}
