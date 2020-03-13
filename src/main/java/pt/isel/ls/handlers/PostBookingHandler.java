package pt.isel.ls.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import pt.isel.ls.router.RequestParameters;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import pt.isel.ls.view.ExceptionView;

public class PostBookingHandler implements RouteHandler {
    private DataSource dataSource;
    public PostBookingHandler(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) {
        try (Connection conn = dataSource.getConnection()) {
            int rid = Integer.parseInt(request.getPathParameter("rid"));
            int uid = Integer.parseInt(request.getPathParameter("rid"));
            String begin = request.getParameter("begin").get(0);
            String end = request.getParameter("end").get(0);
            //TODO: Make sure these Strings are TIMESTAMPs
            Timestamp b = Timestamp.valueOf(begin);
            Timestamp e = Timestamp.valueOf(end);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO BOOKING (begin,\"end\",rid,uid) VALUES (?,?,?,?);");
            stmt.setTimestamp(1,b);
            stmt.setTimestamp(2,e);
            stmt.setInt(3,rid);
            stmt.setInt(4,uid);

            ResultSet res = stmt.executeQuery();
            return new RouteResponse(null);
        } catch (RequestParameters.ParameterNotFoundException | SQLException e) {
            return new RouteResponse(new ExceptionView(e)).setStatusCode(500);
        }
    }
}
