package pt.isel.ls.router;

import java.util.List;
import org.junit.Test;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.RouteRequest.ParameterNotFoundException;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.response.HandlerResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RouterTest {

    @Test
    public void testRouteWithNoParameters() throws RouteException {
        Router router = new Router();
        router.registerRoute(Method.GET, RouteTemplate.of("/example"), request -> {
            assertEquals(1, request.getPath().getPathSegments().length);
            assertEquals("example", request.getPath().getPathSegments()[0]);
            return new HandlerResponse(null);
        });

        RouteRequest request = RouteRequest.of("GET /example");
        assertEquals(200, router.getHandler(request)
                .execute(request).getStatusCode());
    }

    @Test
    public void testRouteWithPathParameters() throws RouteException {
        Router router = new Router();
        router.registerRoute(Method.GET, RouteTemplate.of("/test/{param}"), request -> {
            assertEquals(2, request.getPath().getPathSegments().length);
            assertEquals("test", request.getPath().getPathSegments()[0]);
            try {
                assertEquals("abc", request.getPathParameter("param").toString());
            } catch (ParameterNotFoundException e) {
                fail(e.getMessage());
            }

            return new HandlerResponse(null);
        });

        RouteRequest request1 = RouteRequest.of("GET /test/abc");
        RouteRequest request2 = RouteRequest.of("GET /test");
        assertEquals(200, router.getHandler(request1)
                .execute(request1).getStatusCode());

        assertEquals(404, router.getHandler(request2)
                .execute(request2).getStatusCode());
    }

    @Test
    public void testRouteWithParameters() throws RouteException {
        Router router = new Router();
        router.registerRoute(Method.GET, RouteTemplate.of("/test"), request -> {
            try {
                List<Parameter> a = request.getParameter("a");
                List<Parameter> b = request.getParameter("b");

                assertEquals(1, a.size());
                assertEquals(1, b.size());

                assertEquals("b", a.get(0).toString());
                assertEquals("a", b.get(0).toString());

            } catch (ParameterNotFoundException e) {
                fail(e.getMessage());
            }

            return new HandlerResponse(null);
        });

        RouteRequest request = RouteRequest.of("GET /test a=b&b=a");
        assertEquals(200, router.getHandler(request)
                .execute(request).getStatusCode());
    }

}
