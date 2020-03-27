package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

public class HandlersTestUtils {

    static boolean routeResponseEquals(RouteResponse a, RouteResponse b) {
        StringWriter sw = new StringWriter();
        String obtained;
        try (PrintWriter pw = new PrintWriter(sw)) {
            a.getView().render(pw);
            obtained = sw.toString();
        }
        sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            b.getView().render(pw);
        }

        return sw.toString().equals(obtained);
    }
}
