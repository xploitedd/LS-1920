package pt.isel.ls.view;

public class ExceptionView extends MessageView {

    public ExceptionView(Exception exception) {
        super(exception.getMessage());
    }

}
