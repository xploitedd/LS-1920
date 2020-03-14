package pt.isel.ls.view;

public class ThrowableView extends MessageView {

    public ThrowableView(Throwable throwable) {
        super(throwable.getMessage());
    }

}
