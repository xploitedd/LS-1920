package pt.isel.ls.view;

public enum ViewType {

    HTML("text/html"),
    TEXT("text/plain");

    private final String name;

    ViewType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ViewType of(String name) {
        for (ViewType vt : values()) {
            if (vt.name.equalsIgnoreCase(name)) {
                return vt;
            }
        }

        return null;
    }

}
