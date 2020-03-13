package pt.isel.ls.router;

import java.util.List;
import java.util.Optional;
import pt.isel.ls.router.RequestParameters.ParameterNotFoundException;

public class RouteRequest {

    private final RequestParameters<String> pathParameters;
    private final RequestParameters<List<String>> parameters;
    private final Path path;

    RouteRequest(Path path, RequestParameters<String> pathParameters, RequestParameters<List<String>> parameters) {
        this.path = path;
        this.pathParameters = pathParameters;
        this.parameters = parameters;
    }

    public String getPathParameter(String parameterName) throws ParameterNotFoundException {
        return pathParameters.getParameter(parameterName);
    }

    public Optional<String> getOptionalPathParameter(String parameterName) {
        return pathParameters.getOptionalParameter(parameterName);
    }

    public List<String> getParameter(String parameterName) throws ParameterNotFoundException {
        return parameters.getParameter(parameterName);
    }

    public Optional<List<String>> getOptionalParameter(String parameterName) {
        return parameters.getOptionalParameter(parameterName);
    }

    public Path getPath() {
        return path;
    }

}
