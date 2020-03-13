package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import pt.isel.ls.view.ExceptionView;

public class GetRoomsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetRoomsHandler(DataSource dataSource){
        this.dataSource = dataSource;
    }

    /**
     * Gets Rooms or a specific room
     * @param request
     * @return
     */
    @Override
    public RouteResponse execute(RouteRequest request) {
        try (Connection conn = dataSource.getConnection()){
            PreparedStatement stmt;
            ResultSet res;

            if (request.getOptionalPathParameter("rid").isPresent()){
                int rid = Integer.parseInt(request.getOptionalPathParameter("rid").get());
                stmt = conn.prepareStatement("SELECT * FROM ROOM WHERE rid = ?");
                stmt.setInt(1,rid);
            } else {
                stmt = conn.prepareStatement("SELECT * FROM ROOMS");
            }

            res = stmt.executeQuery();
            return new RouteResponse(null);
        } catch (SQLException e) {
            return new RouteResponse(new ExceptionView(e)).setStatusCode(500);
        }
    }
}
