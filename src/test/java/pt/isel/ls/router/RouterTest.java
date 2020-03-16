package pt.isel.ls.router;

import org.junit.Test;
import pt.isel.ls.router.RouteRequest.ParameterNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RouterTest {

    @Test
    public void testRouteWithNoParameters() {
        Router router = new Router();
        router.registerRoute(Method.GET, RouteTemplate.of("/example"), request -> {
            assertEquals(1, request.getPath().getPathSegments().length);
            assertEquals("example", request.getPath().getPathSegments()[0]);
            return new RouteResponse(null);
        });

        assertEquals(200, router.executeRoute("GET /example").getStatusCode());
    }

    @Test
    public void testRouteWithPathParameters() {
        Router router = new Router();
        router.registerRoute(Method.GET, RouteTemplate.of("/test/{param}"), request -> {
            assertEquals(2, request.getPath().getPathSegments().length);
            assertEquals("test", request.getPath().getPathSegments()[0]);
            try {
                assertEquals("abc", request.getPathParameter("param"));
            } catch (ParameterNotFoundException e) {
                fail(e.getMessage());
            }

            return new RouteResponse(null);
        });

        assertEquals(200, router.executeRoute("GET /test/abc").getStatusCode());
    }

}
