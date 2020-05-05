package pt.isel.ls.app.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.HeaderType;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.Path;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.view.ViewType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AppServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(AppServlet.class);

    private final Router router;

    public AppServlet(Router router) {
        this.router = router;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        Method method = Method.valueOf(req.getMethod());
        Optional<Path> path = Path.of(req.getRequestURI());
        if (path.isEmpty()) {
            LOG.error("Invalid request received: " + req.getRequestURI());
            // bad request
            return;
        }

        HashMap<String, List<Parameter>> parameters = processParameters(req.getParameterMap());
        HashMap<HeaderType, String> headers = new HashMap<>();
        Enumeration<String> headerNames = req.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                HeaderType type = HeaderType.of(name);
                headers.put(type, req.getHeader(name));
            }
        }

        RouteRequest request = new RouteRequest(method, path.get(), parameters, headers);
        try {
            HandlerResponse response = router.getHandler(request).execute(request);
            resp.setStatus(response.getStatusCode());
            PrintWriter pw = new PrintWriter(resp.getOutputStream());

            ViewType viewType = request.getHeaderValue(HeaderType.Accept)
                    .map(ViewType::of)
                    .orElse(ViewType.HTML);

            Charset utf8 = StandardCharsets.UTF_8;
            resp.setContentType(viewType.getName());
            resp.setCharacterEncoding(utf8.name());

            response.getView().render(viewType, pw);
            pw.close();
        } catch (RouteException | IOException e) {
            resp.setStatus(500);
            LOG.error("Error while executing the response: " + e.getMessage());
        }
    }

    private static HashMap<String, List<Parameter>> processParameters(Map<String, String[]> parameters) {
        HashMap<String, List<Parameter>> parameterMap = new HashMap<>();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            ArrayList<Parameter> list = new ArrayList<>(values.length);
            for (String value : values) {
                list.add(new Parameter(value));
            }

            parameterMap.put(key, list);
        }

        return parameterMap;
    }

}
