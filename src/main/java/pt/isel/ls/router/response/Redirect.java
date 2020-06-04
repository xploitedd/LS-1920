package pt.isel.ls.router.response;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.parameter.Parameter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Redirect {

    public static final String LOCATION_HEADER = "Location";

    private final HashMap<String, List<Parameter>> parameters = new HashMap<>();
    private final HandlerResponse response;
    private final Class<? extends RouteHandler> handler;
    private final Object[] pathParams;

    public Redirect(HandlerResponse response, Class<? extends RouteHandler> handler, Object... pathParams) {
        this.response = response;
        this.handler = handler;
        this.pathParams = pathParams;
    }

    public Redirect addParameter(String key, String value) {
        List<Parameter> list = Optional.ofNullable(parameters.get(key))
                .orElse(new ArrayList<>());

        list.add(new Parameter(value));
        // add in case it's a new list
        parameters.put(key, list);
        return this;
    }

    public Class<? extends RouteHandler> getHandler() {
        return handler;
    }

    public HandlerResponse getResponse() {
        return response;
    }

    public Object[] getPathParameters() {
        return pathParams;
    }

    public String getParametersString() {
        StringBuilder sb = new StringBuilder("?");
        for (String key : parameters.keySet()) {
            List<Parameter> values = parameters.get(key);
            for (Parameter value : values) {
                sb.append(urlEncode(key))
                        .append("=")
                        .append(urlEncode(value.toString()))
                        .append("&");
            }
        }

        return sb.substring(0, sb.length() - 1);
    }

    private static String urlEncode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }

}
