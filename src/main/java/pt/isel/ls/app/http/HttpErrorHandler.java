package pt.isel.ls.app.http;

import org.eclipse.jetty.server.handler.ErrorHandler;
import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.router.Router;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.ViewType;
import pt.isel.ls.view.misc.ExceptionView;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.Writer;

public class HttpErrorHandler extends ErrorHandler {

    private final Router router;

    /**
     * Creates a new HTTP error handler
     * @param router Application Router
     */
    public HttpErrorHandler(Router router) {
        this.router = router;
    }

    @Override
    protected void writeErrorPage(HttpServletRequest req, Writer wr, int code, String msg, boolean ss) {
        ViewHandler viewHandler = new ViewHandler(router);

        // just get the exception message, without the exception fully-qualified name
        String error = msg.split(": ", 2)[1];
        ExceptionView view = new ExceptionView(new AppException(code + " " + error));

        PrintWriter pw = new PrintWriter(wr);
        viewHandler.render(view, ViewType.HTML, pw);
        pw.close();
    }

}
