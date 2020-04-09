package pt.isel.ls.view;

public class IdentifierView extends MessageView {

    public IdentifierView(String name, int id) {
        super("Successfully created " + name + " with unique identifier " + id);
    }

    public IdentifierView(String action, String name, int id) {
        super("Successfully " + action + " " + name + " with unique identifier " + id);
    }

}
