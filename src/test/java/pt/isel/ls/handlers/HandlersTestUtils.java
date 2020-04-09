package pt.isel.ls.handlers;

import java.io.IOException;
import pt.isel.ls.router.response.RouteResponse;
import pt.isel.ls.view.ViewType;

import java.io.PrintWriter;
import java.io.StringWriter;

public class HandlersTestUtils {

    static boolean routeResponseEquals(RouteResponse a, RouteResponse b) throws IOException {
        StringWriter sw = new StringWriter();
        String obtained;
        try (PrintWriter pw = new PrintWriter(sw)) {
            a.getView().render(ViewType.TEXT, pw);
            obtained = sw.toString();
        }
        sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            b.getView().render(ViewType.TEXT, pw);
        }
        return sw.toString().equals(obtained);
    }
}
