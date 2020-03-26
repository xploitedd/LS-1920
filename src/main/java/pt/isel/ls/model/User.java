package pt.isel.ls.model;

public class User {
    private final int uid;
    private final String name;
    private final String email;

    public User(int uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return uid + " : " + name;
    }
}
