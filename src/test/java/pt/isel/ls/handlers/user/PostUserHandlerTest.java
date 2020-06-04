package pt.isel.ls.handlers.user;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.misc.IdentifierView;

public class PostUserHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        provider.execute(conn -> {
            UserQueries userQueries = new UserQueries(conn);
            userQueries.createNewUser("teste", "teste@teste.com");

            return null;
        });
        PostUserHandler pbh = new PostUserHandler(provider);
        router = new Router();
        router.registerRoute(pbh);
    }

    @Test
    public void createNewUser() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "POST /users name=TesteUser1&email=TesteUser1@teste.com");

        HandlerResponse response = router.getHandler(request).execute(request);
        Assert.assertTrue(response.getView() instanceof IdentifierView);
    }

    @Test(expected = ValidatorException.class)
    public void createExistingUser() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "POST /users name=teste&email=teste@teste.com");

        HandlerResponse response = router.getHandler(request).execute(request);
        Assert.assertTrue(response.getView() instanceof IdentifierView);

    }
}
