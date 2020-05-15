package pt.isel.ls.handlers.room;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
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

    private static final int MIN_CAPACITY = 2;

    public PostRoomHandler(ConnectionProvider provider) {
        super(
                Method.POST,
                "/rooms",
                "Creates a new room",
                provider
        );
    }

    /**
     * Creates a new room
     * @param request The request to be executed
     * @return a new HandlerResponse
     * @throws RouteException any exception that occurred
     */
    @Override
    public HandlerResponse execute(Router router, RouteRequest request) {
        Optional<List<Parameter>> optLabels = request.getOptionalParameter("label");
        String desc = request.getOptionalParameter("description")
                .map(Object::toString).orElse(null);

        String name = request.getParameter("name").get(0).toString();
        int capacity = request.getParameter("capacity").get(0).toInt();
        String location = request.getParameter("location").get(0).toString();

        if (capacity < MIN_CAPACITY) {
            throw new RouteException("Room capacity should be at least " + MIN_CAPACITY);
        }

        Room inserted = provider.execute(handler -> {
            List<Label> labels = new LinkedList<>();
            if (optLabels.isPresent()) {
                LabelQueries labelQueries = new LabelQueries(handler);
                for (Parameter label : optLabels.get()) {
                    labels.add(labelQueries.getLabel(label.toString()));
                }
            }

            return new RoomQueries(handler).createNewRoom(name, location, capacity, desc, labels);
        });

        return new HandlerResponse(new IdentifierView("room",inserted.getRid()));
    }

}
