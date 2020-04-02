package pt.isel.ls.router.request;

public enum HeaderType {

    Accept("accept"),
    FileName("file-name");

    private String name;

    HeaderType(String name) {
        this.name = name;
    }

    public static HeaderType of(String name) {
        for (HeaderType type : values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }

        return null;
    }

}
