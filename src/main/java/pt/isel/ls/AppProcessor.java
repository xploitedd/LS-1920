package pt.isel.ls;

import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.HeaderType;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.view.ExceptionView;
import pt.isel.ls.view.ViewType;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Optional;

public class AppProcessor {

    private static final OutputStream DEFAULT_STREAM = System.out;
    private static final PrintWriter DEFAULT_WRITER = new PrintWriter(DEFAULT_STREAM);

    private final Router router;

    public AppProcessor(Router router) {
        this.router = router;
    }

    public void processInput(String input) {
        PrintWriter printWriter = null;
        try {
            RouteRequest request = RouteRequest.of(input);
            ViewType viewType = Optional.ofNullable(
                    ViewType.of(request.getHeaderValue(HeaderType.Accept)
                            .orElse(null))
            ).orElse(ViewType.TEXT);

            printWriter = getPrintWriter(request);
            HandlerResponse response = router.getHandler(request)
                    .execute(request);

            response.getView().render(viewType, printWriter);
        } catch (RouteException e) {
            new ExceptionView(e).render(ViewType.TEXT, DEFAULT_WRITER);
        } finally {
            // check that the PrintWriter isn't null and the object is the same as
            // the DEFAULT_WRITER, because we don't want to close the DEFAULT_WRITER
            // since it should only be closed when terminating the App
            if (printWriter != null && printWriter != DEFAULT_WRITER) {
                printWriter.close();
            }
        }
    }

    private PrintWriter getPrintWriter(RouteRequest request) {
        return request.getHeaderValue(HeaderType.FileName)
                .map(this::fileToPrintWriter)
                .orElse(DEFAULT_WRITER);
    }

    private PrintWriter fileToPrintWriter(String fileName) {
        try {
            return new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
