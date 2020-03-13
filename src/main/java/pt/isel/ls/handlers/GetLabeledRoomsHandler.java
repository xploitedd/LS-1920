package pt.isel.ls.handlers;

import pt.isel.ls.router.RequestParameters;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetLabeledRoomsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetLabeledRoomsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets all of the Rooms labeled with a specific Label
     * @param request Request information
     * @return routeResponse
     */
    @Override
    public RouteResponse execute(RouteRequest request) {
        try (Connection conn = dataSource.getConnection()){

            int lid = Integer.parseInt(request.getPathParameter("lid"));
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ROOM WHERE rid IN (SELECT rid FROM ROOM_LABEL WHERE lid = ?)");
            stmt.setInt(1,lid);
            ResultSet res = stmt.executeQuery();

        } catch (RequestParameters.ParameterNotFoundException | SQLException e) {
            return new RouteResponse().setException(e);
        }

        return null;
    }
}
