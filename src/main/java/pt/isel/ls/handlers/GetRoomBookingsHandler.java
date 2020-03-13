package pt.isel.ls.handlers;

import pt.isel.ls.router.RequestParameters;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetRoomBookingsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetRoomBookingsHandler(DataSource dataSource){
        this.dataSource = dataSource;
    }

    /**
     * Gets Bookings from Rooms
     * @param request Request Information
     * @return
     */

    @Override
    public RouteResponse execute(RouteRequest request) {
        try (Connection conn = dataSource.getConnection()){

            int rid = Integer.parseInt(request.getPathParameter("rid"));
            PreparedStatement stmt;

            if(request.getOptionalPathParameter("bid").isPresent()){
                int bid = Integer.parseInt(request.getOptionalPathParameter("bid").get());
                stmt = conn.prepareStatement("SELECT * FROM BOOKING WHERE rid = ? AND bid = ?");
                stmt.setInt(1,rid);
                stmt.setInt(2,bid);
            }else {
                stmt = conn.prepareStatement("SELECT * FROM BOOKING WHERE rid = ?");
                stmt.setInt(1, rid);
            }

            ResultSet res = stmt.executeQuery();

        } catch (RequestParameters.ParameterNotFoundException | SQLException e) {
            return new RouteResponse().setException(e);
        }

        return null;
    }
}
