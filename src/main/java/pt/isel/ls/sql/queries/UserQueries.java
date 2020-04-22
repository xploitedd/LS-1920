package pt.isel.ls.sql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.stream.Stream;

import pt.isel.ls.model.User;

public class UserQueries extends DatabaseQueries {

    public UserQueries(Connection conn) {
        super(conn);
    }

    /**
     * Creates a new User
     * @param name name of the user
     * @param email email of the user
     * @return the created user
     * @throws Throwable any exception that occurs
     */
    public User createNewUser(String name, String email) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO \"user\" (email, name) VALUES (?, ?);"
        );

        stmt.setString(1, email);
        stmt.setString(2, name);
        stmt.execute();

        return getUser(name, email);
    }

    /**
     * Get User by uid
     * @param uid id of the user
     * @return an User
     * @throws Exception any exception that occurs
     */
    public User getUser(int uid) throws Exception {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT name, email FROM \"user\" WHERE uid = ?;"
        );

        ret.setInt(1, uid);
        ResultSet rs = ret.executeQuery();
        rs.next();

        return new User(uid, rs.getString("name"), rs.getString("email"));
    }

    /**
     * Get user by name and email
     * @param name name of the user
     * @param email email of the user
     * @return an User
     * @throws Exception any exception that occurs
     */
    public User getUser(String name, String email) throws Exception {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT uid FROM \"user\" WHERE email = ? AND name = ?;"
        );

        ret.setString(1, email);
        ret.setString(2, name);
        ResultSet rs = ret.executeQuery();
        rs.next();

        return new User(rs.getInt("uid"), name, email);
    }

    /**
     * Get all users
     * @return all users
     * @throws Exception any exception that occurs
     */
    public Stream<User> getUsers() throws Exception {
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

        return results.stream();
    }

}
