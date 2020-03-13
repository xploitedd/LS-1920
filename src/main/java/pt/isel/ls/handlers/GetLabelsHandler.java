package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pt.isel.ls.view.ExceptionView;

public class GetLabelsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetLabelsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets all existing Labels
     * @param request Request information
     * @return routeResponse
     */
    @Override
    public RouteResponse execute(RouteRequest request){
        try (Connection conn = dataSource.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM LABEL");
            ResultSet res = stmt.executeQuery();
            return new RouteResponse(null);
        } catch (SQLException e) {
            return new RouteResponse(new ExceptionView(e)).setStatusCode(500);
        }
    }
}
