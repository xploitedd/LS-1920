package pt.isel.ls.handlers;

import pt.isel.ls.router.RequestParameters;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pt.isel.ls.view.ExceptionView;

public class GetUserBookingsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetUserBookingsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets booking booked by a user
     * @param request The route request
     * @return
     */
    @Override
    public RouteResponse execute(RouteRequest request) {
        try (Connection conn = dataSource.getConnection()) {
            int uid = Integer.parseInt(request.getPathParameter("uid"));
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM BOOKING WHERE uid = ?");
            stmt.setInt(1, uid);
            ResultSet res = stmt.executeQuery();

            return new RouteResponse(null);
        } catch (RequestParameters.ParameterNotFoundException | SQLException e) {
            return new RouteResponse(new ExceptionView(e)).setStatusCode(500);
        }
    }
}
