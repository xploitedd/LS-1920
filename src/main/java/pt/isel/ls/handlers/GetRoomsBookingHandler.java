package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetRoomsBookingHandler implements RouteHandler {

    private DataSource dataSource;

    public GetRoomsBookingHandler(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws SQLException {
        int rid = 0; //Zero for now until routeResponse is complete
        int bid = 0;
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM BOOKING WHERE rid = ? AND bid = ?");
        stmt.setInt(1,rid);
        stmt.setInt(2,bid);
        ResultSet res = stmt.executeQuery();
        conn.close();
        return null;
    }
}
