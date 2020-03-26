package pt.isel.ls.view.console;

import pt.isel.ls.router.RouteException;

public class RouteExceptionView extends MessageView {

    public RouteExceptionView(RouteException throwable) {
        super(throwable.getMessage());
    }

}
