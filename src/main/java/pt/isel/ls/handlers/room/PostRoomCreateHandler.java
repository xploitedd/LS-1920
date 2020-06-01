package pt.isel.ls.handlers.room;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.room.RoomCreateView;

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
        try {
            Room newRoom = new PostRoomHandler(provider).createRoom(request);
            return new HandlerResponse()
                    .redirect(GetRoomHandler.class, newRoom.getRid());
        } catch (AppException e) {
            Iterable<Label> labels = provider.execute(handler -> new LabelQueries(handler)
                    .getLabels()
                    .collect(Collectors.toList()));

            return new HandlerResponse(new RoomCreateView(labels, e.getMessage()))
                    .setStatusCode(e.getStatusCode());
        }
    }
}
