package pt.isel.ls.handlers.misc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.view.MessageView;

public class GetTimeHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() {
        GetTimeHandler gth = new GetTimeHandler();
        router = new Router();
        router.registerRoute(gth);
    }

    @Test
    public void getUserById() throws RouteException {
        RouteRequest request = RouteRequest.of("GET /time");

        HandlerResponse response = router.getHandler(request).execute(router, request);

        Assert.assertTrue(response.getView() instanceof MessageView);
    }
}
