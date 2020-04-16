package pt.isel.ls.handlers;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteResponse;
import pt.isel.ls.view.MessageView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetTimeHandler implements RouteHandler {
    /**
     * Gets the current time in ISO format
     * @param request The route request
     * @return returns a RouteResponse with a MessageView for the router
     */
    @Override
    public RouteResponse execute(RouteRequest request) {
        return new RouteResponse(new MessageView("Current time: "
                + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
    }

}
