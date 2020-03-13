package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserHandler implements RouteHandler {

    private DataSource dataSource;

    public GetUserHandler(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws SQLException {
        int uid = 1;
        Connection conn =dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USER WHERE uid = ?");
        stmt.setInt(1,uid);
        ResultSet res = stmt.executeQuery();
        conn.close();
        return null;
    }
}
