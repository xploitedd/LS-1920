package pt.isel.ls.app.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.router.request.HeaderType;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.Path;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.Redirect;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.ViewType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    private final ViewHandler viewHandler;

    public AppServlet(Router router) {
        this.router = router;
        this.viewHandler = new ViewHandler(router);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processRequest(req, resp);
    }

    /**
     * Processes an HTTP request
     * @param req Request to be processed
     * @param resp Response container
     */
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RouteRequest request = getRequest(req);
        try {
            HandlerResponse response = router.getHandler(request)
                    .execute(request);

            resp.setStatus(response.getStatusCode().getCode());

            if (response.hasRedirect()) {
                Redirect redirect = response.getRedirect();
                String route = router.route(redirect.getHandler(), redirect.getPathParameters());
                if (route != null) {
                    // valid redirect
                    String redStr = route + redirect.getParametersString();
                    resp.setHeader(Redirect.LOCATION_HEADER, redStr);
                    return;
                }
            }

            // need to use a StringWriter because we need to obtain the content length
            // before sending the actual content
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);

            // default view type is ViewType.HTML
            ViewType viewType = request.getHeaderValue(HeaderType.ACCEPT)
                    .map(ViewType::of)
                    .orElse(ViewType.HTML);

            Charset utf8 = StandardCharsets.UTF_8;
            resp.setContentType(viewType.getName());
            resp.setCharacterEncoding(utf8.name());

            viewHandler.render(response.getView(), viewType, pw);
            pw.close();

            byte[] content = writer.toString().getBytes();
            resp.setContentLength(content.length);
            resp.getOutputStream().write(content);
        } catch (AppException e) {
            resp.sendError(e.getStatusCode().getCode(),
                    e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString());
            resp.sendError(StatusCode.INTERNAL_SEVER_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * Get a RouteRequest instance from a HttpServletRequest
     * @param req HTTP request
     * @return a new RouteRequest
     */
    private static RouteRequest getRequest(HttpServletRequest req) {
        Optional<Path> path = Path.of(req.getRequestURI());
        if (path.isEmpty()) {
            LOG.error("Invalid request received: {}", req.getRequestURI());
            // ??? this error should never happen, but just in case
            throw new AppException("Unexpected Invalid Http Path!");
        }

        LOG.debug("new request received. path: {}", path.get());
        Method method = Method.valueOf(req.getMethod());
        return new RouteRequest(
                method,
                path.get(),
                processParameters(req),
                processHeaders(req)
        );
    }

    /**
     * Process the parameters from a HTTP request
     * @param req request where the parameters are
     * @return a new HashMap with the non-empty parameters
     */
    private static HashMap<String, List<Parameter>> processParameters(HttpServletRequest req) {
        Map<String, String[]> parameters = req.getParameterMap();
        HashMap<String, List<Parameter>> parameterMap = new HashMap<>();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            ArrayList<Parameter> list = null;
            for (String value : values) {
                if (!value.isEmpty() && !value.isBlank()) {
                    if (list == null) {
                        list = new ArrayList<>(values.length);
                    }

                    list.add(new Parameter(value));
                }
            }

            if (list != null) {
                parameterMap.put(key, list);
            }
        }

        return parameterMap;
    }

    /**
     * Process the headers from a HTTP request
     * @param req request with headers
     * @return a new HashMap with the application-supported headers
     */
    private static HashMap<HeaderType, String> processHeaders(HttpServletRequest req) {
        HashMap<HeaderType, String> headers = new HashMap<>();
        Enumeration<String> headerNames = req.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                HeaderType type = HeaderType.of(name);
                if (type != null) {
                    headers.put(type, req.getHeader(name));
                }
            }
        }

        return headers;
    }

}
