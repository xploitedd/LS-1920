package pt.isel.ls.handlers.label;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.misc.IdentifierView;


public class PostLabelHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        provider.execute(conn -> {
            LabelQueries labelQueries = new LabelQueries(conn);
            labelQueries.createNewLabel("Teste");

            return null;
        });
        PostLabelHandler pbh = new PostLabelHandler(provider);
        router = new Router();
        router.registerRoute(pbh);
    }

    @Test
    public void createNewLabel() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "POST /labels name=teste1");

        HandlerResponse response = router.getHandler(request).execute(router, request);
        Assert.assertTrue(response.getView() instanceof IdentifierView);
    }
}
