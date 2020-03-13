package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostBookingHandler implements RouteHandler {
    private DataSource dataSource;
    public PostBookingHandler(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ROOM WHERE rid IN (SELECT rid FROM ROOM_LABEL WHERE lid = ?)");
        ResultSet res = stmt.executeQuery();
        conn.close();
        return null;
    }
}
