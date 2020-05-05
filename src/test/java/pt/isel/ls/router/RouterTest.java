package pt.isel.ls.router;

import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.handlers.Handler;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.handlers.room.GetRoomsHandler;
import pt.isel.ls.handlers.room.PostRoomHandler;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.exceptions.router.RouteException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RouterTest {

    @Before
    public void beforeEach() {
        DatasourceUtils.executeFile("CreateTables.sql");
    }

    @Test
    public void testRouteWithNoParameters() throws RouteException {
        Router router = new Router();
        router.registerRoute(new GetRoomsHandler(null));
        Handler handler = router.getHandler(RouteRequest.of(
                "GET /rooms"));

        assertNotNull(handler);
        assertTrue(handler instanceof RouteHandler);
    }

    @Test
    public void testRouteWithPathParameters() throws RouteException {
        Router router = new Router();
        router.registerRoute(new GetRoomHandler(null));
        Handler handler = router.getHandler(RouteRequest.of(
                "GET /rooms/1"));

        assertNotNull(handler);
        assertTrue(handler instanceof RouteHandler);
    }

    @Test
    public void testRouteWithParameters() throws RouteException {
        Router router = new Router();
        router.registerRoute(new PostRoomHandler(null));
        Handler handler = router.getHandler(RouteRequest.of(
                "POST /rooms name=A"));

        assertNotNull(handler);
        assertTrue(handler instanceof RouteHandler);
    }

}
