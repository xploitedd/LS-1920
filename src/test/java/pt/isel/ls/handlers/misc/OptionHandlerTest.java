package pt.isel.ls.handlers.misc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.view.TableView;

public class OptionHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() {
        router = new Router();
        OptionHandler oh = new OptionHandler(router);
        router.registerRoute(oh);
    }

    @Test
    public void getUserById() throws RouteException {
        RouteRequest request = RouteRequest.of("OPTION /");

        HandlerResponse response = router.getHandler(request).execute(router, request);

        Assert.assertTrue(response.getView() instanceof TableView);
    }
}
