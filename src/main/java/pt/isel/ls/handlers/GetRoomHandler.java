package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetRoomHandler implements RouteHandler {

    private DataSource dataSource;

    public GetRoomHandler(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws SQLException {
        int rid = 0; //Placeholder until routeresponse is ready
        Connection conn =dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ROOM WHERE rid = ?");
        stmt.setInt(1,rid);
        ResultSet res = stmt.executeQuery();
        conn.close();
        return null;
    }
}
