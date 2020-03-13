package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class GetRoomsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetRoomsHandler(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt;
        ResultSet res;

        if(request.getOptionalPathParameter("rid").isPresent()){
            int rid = Integer.parseInt(request.getOptionalPathParameter("rid").get());
            stmt = conn.prepareStatement("SELECT * FROM ROOM WHERE rid = ?");
            stmt.setInt(1,rid);
            res = stmt.executeQuery();
        }

        stmt = conn.prepareStatement("SELECT * FROM ROOMS");
        res = stmt.executeQuery();
        conn.close();
        return null;
    }
}
