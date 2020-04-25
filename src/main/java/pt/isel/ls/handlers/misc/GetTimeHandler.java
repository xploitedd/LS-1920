package pt.isel.ls.handlers.misc;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.MessageView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetTimeHandler extends RouteHandler {

    public GetTimeHandler() {
        super(
                Method.GET,
                "/time",
                "Gets the current time in ISO format"
        );
    }

    /**
     * Gets the current time in ISO format
     * @param request The route request
     * @return returns a HandlerResponse with a MessageView for the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        return new HandlerResponse(new MessageView("Current time: "
                + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
    }

}
