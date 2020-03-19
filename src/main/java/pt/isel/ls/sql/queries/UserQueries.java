package pt.isel.ls.sql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import pt.isel.ls.model.User;

public class UserQueries extends DatabaseQueries {

    public UserQueries(Connection conn) {
        super(conn);
    }

    public User createNewUser(String name, String email) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO \"user\" (email,name) VALUES (?,?);"
        );
        stmt.setString(1,email);
        stmt.setString(2,name);
        stmt.execute();
        return getUser(name, email);
    }

    public User getUser(String name, String email) throws Throwable {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT uid FROM \"user\" WHERE email = ? AND name = ?;"
        );
        ret.setString(1,email);
        ret.setString(2,name);
        ResultSet rs = ret.executeQuery();
        rs.next();
        int uid = rs.getInt("uid");
        return new User(uid,name,email);
    }

    public Iterable<User> getUsers() throws Throwable {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT * FROM \"user\""
        );
        ResultSet rs = ret.executeQuery();
        LinkedList<User> results = new LinkedList<>();
        while (rs.next()) {
            int uid = rs.getInt("uid");
            String name = rs.getString("name");
            String email = rs.getString("email");
            results.add(new User(uid, name, email));
        }
        return results;
    }

}
