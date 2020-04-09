package pt.isel.ls.view;

import pt.isel.ls.router.response.RouteException;

public class RouteExceptionView extends MessageView {

    public RouteExceptionView(RouteException throwable) {
        super(throwable.getMessage());
    }

}
