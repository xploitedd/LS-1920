package pt.isel.ls.view.console;

public class ThrowableView extends MessageView {

    public ThrowableView(Throwable throwable) {
        super(throwable.getMessage());
    }

}
