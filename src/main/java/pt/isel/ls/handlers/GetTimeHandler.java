package pt.isel.ls.handlers;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.MessageView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetTimeHandler implements RouteHandler {

    private static final String DESCRIPTION = "Gets the current time in ISO format";

    /**
     * Gets the current time in ISO format
     * @param request The route request
     * @return returns a RouteResponse with a MessageView for the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        return new HandlerResponse(new MessageView("Current time: "
                + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
