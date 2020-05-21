package pt.isel.ls.handlers.user;

import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;

public class GetUserHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        provider.execute(conn -> {
            UserQueries userQueries = new UserQueries(conn);
            userQueries.createNewUser("TestUser", "TestUser@test.com");

            return null;
        });

        GetUserHandler guh = new GetUserHandler(provider);
        router = new Router();
        router.registerRoute(guh);
    }

    @Test
    public void getUserById() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "GET /users/1");

        HandlerResponse response = router.getHandler(request).execute(router, request);

        //Assert.assertTrue(response.getView() instanceof TableView);
    }

    @Test(expected = RouteException.class)
    public void getUserDoesNotExist() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "GET /users/2");

        router.getHandler(request).execute(router, request);
    }
}
