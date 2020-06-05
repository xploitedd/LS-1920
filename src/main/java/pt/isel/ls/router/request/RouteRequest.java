package pt.isel.ls.router.request;

import pt.isel.ls.exceptions.parameter.ParameterNotFoundException;
import pt.isel.ls.exceptions.router.RouteParsingException;
import pt.isel.ls.router.RouterUtils;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.router.request.validator.Parameter;
import pt.isel.ls.router.request.validator.ParameterValueList;

import java.util.HashMap;
import java.util.Optional;

public class RouteRequest {

    private final Method method;
    private final Path path;
    private final HashMap<String, ParameterValueList> parameters;
    private final HashMap<HeaderType, String> headers;

    private HashMap<String, Parameter> pathParameters = new HashMap<>();

    /**
     * Creates a new instance of RouteRequest, representing a new request
     * @param method method used on the request
     * @param path path of the request
     * @param parameters parameters of the request
     */
    public RouteRequest(Method method, Path path,
                         HashMap<String, ParameterValueList> parameters,
                         HashMap<HeaderType, String> headers) {

        this.method = method;
        this.path = path;
        this.parameters = parameters;
        this.headers = headers;
    }

    public Optional<String> getHeaderValue(HeaderType type) {
        return Optional.ofNullable(headers.get(type));
    }

    /**
     * Gets a mandatory path parameter
     * If the parameter does not exist it throws an exception
     * @param parameterName name of the parameter
     * @return parameter string value
     * @throws ParameterNotFoundException in case the parameter does not exist
     */
    public Parameter getPathParameter(String parameterName) throws ParameterNotFoundException {
        return Optional.ofNullable(pathParameters.get(parameterName))
                .orElseThrow(() -> new ParameterNotFoundException(parameterName));
    }

    /**
     * Gets a mandatory parameter
     * If the parameter does not exist it throws an exception
     * @param parameterName name of the parameter
     * @return parameter list
     * @throws ParameterNotFoundException in case the parameter does not exist
     */
    public ParameterValueList getParameter(String parameterName) throws ParameterNotFoundException {
        return Optional.ofNullable(parameters.get(parameterName))
                .orElseThrow(() -> new ParameterNotFoundException(parameterName));
    }

    /**
     * Sets the path parameters
     * This method should be used by the Router when the path
     * parameters become available
     * @param pathParameters path parameters map
     */
    public void setPathParameters(HashMap<String, Parameter> pathParameters) {
        this.pathParameters = pathParameters;
    }

    /**
     * Gets this Request path
     * @return request path
     */
    public Path getPath() {
        return path;
    }

    /**
     * Gets this request method
     * @return request method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Parses a string request into a new RouteRequest instance
     * @param request request to be parsed
     * @return a new instance of RouteRequest
     */
    public static RouteRequest of(String request) {
        try {
            String[] requestParts = request.split(" ");
            if (requestParts.length < 2) {
                throw new RouteException("Path not valid!");
            }

            Method method = Method.fromString(requestParts[0]);
            Optional<Path> path = Path.of(requestParts[1]);
            if (path.isEmpty()) {
                throw new RouteException("Path not valid!");
            }

            Optional<HashMap<String, ParameterValueList>> parameters = Optional.empty();
            Optional<HashMap<HeaderType, String>> headers = Optional.empty();

            // if headers are present
            if (requestParts.length > 2) {
                // if parameters are present
                if (requestParts.length > 3) {
                    headers = Optional.of(parseHeaders(requestParts[2]));
                    parameters = Optional.of(parseParameters(requestParts[3]));
                } else {
                    // use try-catch to verify if the third request part are the parameters or the headers
                    try {
                        parameters = Optional.of(parseParameters(requestParts[2]));
                    } catch (RouteException e) {
                        headers = Optional.of(parseHeaders(requestParts[2]));
                    }
                }
            }

            return new RouteRequest(method, path.get(),
                    parameters.orElseGet(HashMap::new), headers.orElseGet(HashMap::new));
        } catch (Exception exception) {
            throw new RouteParsingException(exception.getMessage());
        }
    }

    /**
     * Parse the parameters of a parameter section
     * @param parameterSection parameter section to be parsed
     * @return parameter map
     */
    private static HashMap<String, ParameterValueList> parseParameters(String parameterSection) {
        HashMap<String, ParameterValueList> parameters = new HashMap<>();
        RouterUtils.forEachKeyValue(parameterSection, "&", "=", (key, value) -> {
            ParameterValueList values = Optional.ofNullable(parameters.get(key))
                    .orElse(new ParameterValueList(key));

            values.add(new Parameter(value));
            parameters.put(key, values);
        });

        return parameters;
    }

    /**
     * Parse the headers from a string
     * @param headerSection headers string
     * @return headers map
     */
    private static HashMap<HeaderType, String> parseHeaders(String headerSection) {
        HashMap<HeaderType, String> headers = new HashMap<>();
        RouterUtils.forEachKeyValue(headerSection, "\\|", ":", (key, value) -> {
            HeaderType type = HeaderType.of(key);
            headers.put(type, value);
        });

        return headers;
    }

}
