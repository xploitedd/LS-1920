package pt.isel.ls.model;

public final class User {

    private final int uid;
    private final String name;
    private final String email;

    /**
     * Creates a new User
     * @param uid id of the user
     * @param name name of the user
     * @param email email of the user
     */
    public User(int uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    /**
     * Get User Id
     * @return User Id
     */
    public int getUid() {
        return uid;
    }

    /**
     * Get User Name
     * @return User Name
     */
    public String getName() {
        return name;
    }

    /**
     * Get User Email
     * @return User Email
     */
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return uid + " : " + name;
    }

}
