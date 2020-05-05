package pt.isel.ls.handlers.label;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.view.TableView;

public final class GetLabeledRoomsHandler extends RouteHandler {

    public GetLabeledRoomsHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/labels/{lid}/rooms",
                "Gets all of the rooms with a certain label",
                provider
        );
    }

    /**
     * Gets all of the rooms with a certain label
     * @param request The route request
     * @return returns a HandlerResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        int lid = request.getPathParameter("lid").toInt();

        Table table = new Table("RID", "Name", "Location", "Capacity", "Description");
        provider.execute(handler -> new RoomLabelQueries(handler)
                .getLabeledRooms(lid))
                .forEach(room ->
                        table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                                String.valueOf(room.getCapacity()), room.getDescription()));

        return new HandlerResponse(new TableView("Rooms with Label: " + lid, table));
    }

}
