package pt.isel.ls.handlers;

import pt.isel.ls.router.RequestParameters;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import java.sql.SQLException;

public interface RouteHandler {

    RouteResponse execute(RouteRequest request) throws SQLException, RequestParameters.ParameterNotFoundException;

}
