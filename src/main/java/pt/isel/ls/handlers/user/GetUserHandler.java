package pt.isel.ls.handlers.user;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.model.User;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.user.UserView;

public final class GetUserHandler extends RouteHandler {

    public GetUserHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/users/{uid}",
                "Get a specific user",
                provider
        );
    }

    /**
     * Get a specific user
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        Parameter paramUid = request.getPathParameter("uid");
        Table table = new Table("User Id", "Name", "Email");
        int uid = paramUid.toInt();
        User user = provider.execute(handler -> new UserQueries(handler)
                .getUser(uid));

        table.addTableRow(String.valueOf(user.getUid()), user.getName(), user.getEmail());
        return new HandlerResponse(new UserView("User: " + uid, table));
    }

}
