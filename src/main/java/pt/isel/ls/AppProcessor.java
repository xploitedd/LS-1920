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

    /**
     * Creates a new Processor that handles all router requests
     * @param router router where the request are going to be handled
     */
    public AppProcessor(Router router) {
        this.router = router;
    }

    /**
     * Processes a string of input
     * @param input input string
     */
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
            printWriter.flush();
        } catch (RouteException e) {
            new ExceptionView(e).render(ViewType.TEXT, DEFAULT_WRITER);
            DEFAULT_WRITER.flush();
        } finally {
            // check that the PrintWriter isn't null and the object is the same as
            // the DEFAULT_WRITER, because we don't want to close the DEFAULT_WRITER
            // since it should only be closed when terminating the App
            if (printWriter != null && printWriter != DEFAULT_WRITER) {
                printWriter.close();
            }
        }
    }

    /**
     * Get a print writer from the request headers
     *
     * If no print writer is available or an exception
     * occurred, then it's going to fallback to the
     * default PrintWriter
     * @param request route request
     * @return request PrintWriter, or the fallback one
     */
    private static PrintWriter getPrintWriter(RouteRequest request) {
        return request.getHeaderValue(HeaderType.FileName)
                .map(AppProcessor::fileToPrintWriter)
                .orElse(DEFAULT_WRITER);
    }

    /**
     * Converts a file name to a PrintWriter
     * @param fileName file to be opened for writing
     * @return a new PrintWriter, or null if an exception occurred
     */
    private static PrintWriter fileToPrintWriter(String fileName) {
        try {
            return new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
