package pt.isel.ls.router.request;

public enum HeaderType {

    ACCEPT("accept"),
    FILENAME("file-name");

    private final String name;

    HeaderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static HeaderType of(String name) {
        for (HeaderType type : HeaderType.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }

        return null;
    }

}
