package pt.isel.ls.handlers;

import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.view.TableView;

public final class GetLabeledRoomsHandler implements RouteHandler {

    private static final String DESCRIPTION = "Gets all of the rooms with a certain label";

    private final ConnectionProvider provider;

    public GetLabeledRoomsHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets all of the rooms with a certain label
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        int lid = request.getPathParameter("lid").toInt();

        Table table = new Table("RID", "Name", "Location", "Capacity", "Description");
        provider.execute(conn -> new RoomLabelQueries(conn)
                .getLabeledRooms(lid))
                .forEach(room ->
                        table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                                String.valueOf(room.getCapacity()), room.getDescription()));

        return new HandlerResponse(new TableView(table));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
