package pt.isel.ls.sql.queries;

import java.util.Optional;
import java.util.stream.Stream;

import pt.isel.ls.model.User;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.sql.api.SqlHandler;

public class UserQueries extends DatabaseQueries {

    public UserQueries(SqlHandler handler) {
        super(handler);
    }

    /**
     * Creates a new User
     * @param name name of the user
     * @param email email of the user
     * @return the created user
     */
    public User createNewUser(String name, String email) {
        handler.createUpdate("INSERT INTO \"user\" (email, name) VALUES (?, ?);")
                .bind(email)
                .bind(name)
                .execute();

        return getUser(name, email);
    }

    /**
     * Get User by uid
     * @param uid id of the user
     * @return an User
     */
    public User getUser(int uid) {
        Optional<User> user = handler
                .createQuery("SELECT * FROM \"user\" WHERE uid = ?;")
                .bind(uid)
                .mapToClass(User.class)
                .findFirst();

        if (user.isEmpty()) {
            throw new RouteException("A user with uid " + uid + " was not found!", StatusCode.NOT_FOUND);
        }

        return user.get();
    }

    /**
     * Get user by name and email
     * @param name name of the user
     * @param email email of the user
     * @return an User
     */
    public User getUser(String name, String email) {
        Optional<User> user = handler
                .createQuery("SELECT * FROM \"user\" WHERE email = ? AND name = ?;")
                .bind(email)
                .bind(name)
                .mapToClass(User.class)
                .findFirst();

        if (user.isEmpty()) {
            throw new RouteException("A user was not found!", StatusCode.NOT_FOUND);
        }

        return user.get();
    }

    /**
     * Get all users
     * @return all users
     */
    public Stream<User> getUsers() {
        return handler.createQuery("SELECT * FROM \"user\"")
                .mapToClass(User.class);
    }

}
