package pt.isel.ls.handlers.misc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.view.ExitView;

public class ExitHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() {
        ExitHandler eh = new ExitHandler();
        router = new Router();
        router.registerRoute(eh);
    }

    @Test
    public void getUserById() throws RouteException {
        RouteRequest request = RouteRequest.of("EXIT /");

        HandlerResponse response = router.getHandler(request).execute(request);

        Assert.assertTrue(response.getView() instanceof ExitView);
    }
}
