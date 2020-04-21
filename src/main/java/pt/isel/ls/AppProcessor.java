package pt.isel.ls;

import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.HeaderType;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.view.ExceptionView;
import pt.isel.ls.view.ViewType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class AppProcessor {

    private final Router router;

    public AppProcessor(Router router) {
        this.router = router;
    }

    public void processInput(String input) {
        processInput(input, System.out);
    }

    public void processInput(String input, OutputStream defaultStream) {
        ViewType viewType = null;
        PrintWriter printWriter = null;
        try {
            RouteRequest request = RouteRequest.of(input);
            viewType = ViewType.of(request.getHeaderValue(HeaderType.Accept)
                    .orElse(null));

            defaultStream = parseOutputStream(request, defaultStream);
            printWriter = new PrintWriter(defaultStream);
            HandlerResponse response = router.getHandler(request)
                    .execute(request);

            response.getView().render(viewType, printWriter);
        } catch (RouteException e) {
            printWriter = new PrintWriter(defaultStream);
            new ExceptionView(e).render(viewType, printWriter);
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    private OutputStream parseOutputStream(RouteRequest request, OutputStream defaultStream) {
        return request.getHeaderValue(HeaderType.FileName)
                .map(this::fileToStream).orElse(defaultStream);
    }

    private OutputStream fileToStream(String fileName) {
        try {
            return new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
