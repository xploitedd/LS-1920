package pt.isel.ls.handlers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.Parameter;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;

public final class PostRoomHandler implements RouteHandler {

    private final DataSource dataSource;

    public PostRoomHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws RouteException {
        Optional<List<Parameter>> optLabels = request.getOptionalParameter("label");
        String desc = request.getOptionalParameter("description")
                .map(Object::toString).orElse(null);

        String name = request.getParameter("name").get(0).toString();
        int capacity = request.getParameter("capacity").get(0).toInt();
        String location = request.getParameter("location").get(0).toString();

        Room inserted = new ConnectionProvider(dataSource).execute(conn -> {
            List<Label> labels = new LinkedList<>();
            if (optLabels.isPresent()) {
                LabelQueries labelQueries = new LabelQueries(conn);
                for (Parameter label : optLabels.get()) {
                    labels.add(labelQueries.getLabel(label.toString()));
                }
            }

            return new RoomQueries(conn).createNewRoom(name, location, capacity, desc, labels);
        });

        return new RouteResponse(new IdentifierView("room",inserted.getRid()));

    }
}
