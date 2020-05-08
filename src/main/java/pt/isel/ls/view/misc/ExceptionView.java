package pt.isel.ls.view.misc;

import pt.isel.ls.view.MessageView;

public class ExceptionView extends MessageView {

    public ExceptionView(Exception exception) {
        super(exception.getMessage());
    }

}
