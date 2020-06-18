package pt.isel.ls.router.response.error;

import pt.isel.ls.router.request.RouteRequest;

import java.util.Map;

public class ParameterErrors extends Errors<Map.Entry<String, String>> {

    private final RouteRequest request;

    public ParameterErrors(RouteRequest request) {
        this.request = request;
    }

    public RouteRequest getRequest() {
        return request;
    }

}
