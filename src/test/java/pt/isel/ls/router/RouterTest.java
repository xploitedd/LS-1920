package pt.isel.ls.router;

import java.util.List;
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

    @Test
    public void testRouteWithParameters() {
        Router router = new Router();
        router.registerRoute(Method.GET, RouteTemplate.of("/test"), request -> {
            try {
                List<String> a = request.getParameter("a");
                List<String> b = request.getParameter("b");

                assertEquals(1, a.size());
                assertEquals(1, b.size());

                assertEquals("b", a.get(0));
                assertEquals("a", b.get(0));

            } catch (ParameterNotFoundException e) {
                fail(e.getMessage());
            }

            return new RouteResponse(null);
        });

        assertEquals(200, router.executeRoute("GET /test a=b&b=a").getStatusCode());
    }

}
