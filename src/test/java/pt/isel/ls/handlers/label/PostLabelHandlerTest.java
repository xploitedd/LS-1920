package pt.isel.ls.handlers.label;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.router.RouteTemplate;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.IdentifierView;


public class PostLabelHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        PostLabelHandler pbh = new PostLabelHandler(provider);
        router = new Router();
        router.registerRoute(Method.POST, RouteTemplate.of("/labels"), pbh);
    }

    @Test
    public void createNewLabel() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "POST /labels name = teste1");

        HandlerResponse response = router.getHandler(request).execute(request);
        Assert.assertTrue(response.getView() instanceof IdentifierView);
    }
}
