package pt.isel.ls.view.utils.form;

public enum InputType {

    TEXT("text"),
    NUMBER("number"),
    DATETIME("datetime-local"),
    PASSWORD("password"),
    EMAIL("email");

    private final String type;

    InputType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
