package pt.isel.ls.handlers.label;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.label.LabelView;

public class GetLabelHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        provider.execute(conn -> {
            LabelQueries labelQueries = new LabelQueries(conn);
            labelQueries.createNewLabel("Teste1");

            return null;
        });

        GetLabelHandler glh = new GetLabelHandler(provider);
        router = new Router();
        router.registerRoute(glh);
    }

    @Test
    public void getExistingLabel() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "GET /labels/1");

        HandlerResponse response = router.getHandler(request).execute(request);
        Assert.assertTrue(response.getView() instanceof LabelView);
    }

    @Test(expected = RouteException.class)
    public void getNonExistingLabel() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "GET /labels/2");

        HandlerResponse response = router.getHandler(request).execute(request);
    }

}
