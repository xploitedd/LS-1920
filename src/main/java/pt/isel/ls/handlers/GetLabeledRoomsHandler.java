package pt.isel.ls.handlers;

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

    @Override
    public RouteResponse execute(RouteRequest request) throws SQLException {
        int lid = 0; //Placeholder until RouteResponse is ready
        Connection conn =dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ROOM WHERE rid IN (SELECT rid FROM ROOM_LABEL WHERE lid = ?)");
        stmt.setInt(1,lid);
        ResultSet res = stmt.executeQuery();
        conn.close();
        return null;
    }
}
