package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class PostBookingHandler implements RouteHandler {

    private DataSource dataSource;

    public PostBookingHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        try (Connection conn = dataSource.getConnection()) {
            int rid = Integer.parseInt(request.getPathParameter("rid"));
            int uid = Integer.parseInt(request.getPathParameter("rid"));
            String begin = request.getParameter("begin").get(0);
            String end = request.getParameter("end").get(0);
            //TODO: Make sure these Strings are TIMESTAMPs
            Timestamp b = Timestamp.valueOf(begin);
            Timestamp e = Timestamp.valueOf(end);
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO BOOKING (begin,\"end\",rid,uid) VALUES (?,?,?,?);"
            );

            stmt.setTimestamp(1,b);
            stmt.setTimestamp(2,e);
            stmt.setInt(3,rid);
            stmt.setInt(4,uid);
            stmt.execute();
            // If you find a better way to do this please tell me
            PreparedStatement ret = conn.prepareStatement(
                    "SELECT bid FROM BOOKING WHERE begin = ? AND end = ? AND rid = ? AND uid = ?;"
            );

            ret.setTimestamp(1,b);
            ret.setTimestamp(2,e);
            ret.setInt(3,rid);
            ret.setInt(4,uid);
            ResultSet rs = ret.executeQuery();
            rs.next();
            int bid = rs.getInt("bid");
            return new RouteResponse(new IdentifierView("booking",bid));
        }
    }
}
