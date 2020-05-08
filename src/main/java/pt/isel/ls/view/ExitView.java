package pt.isel.ls.view;

import java.io.PrintWriter;
import pt.isel.ls.exceptions.router.RouteException;

public final class ExitView extends View {

    private final String customText;
    private final int statusCode;

    public ExitView() {
        this("Closing Application. Bye!", 0);
    }

    public ExitView(String customText, int statusCode) {
        super("Closing");
        this.customText = customText;
        this.statusCode = statusCode;
    }

    @Override
    protected void renderText(PrintWriter writer) {
        writer.println(customText);
        writer.close();
        try {
            Thread.sleep(2000);
            System.exit(statusCode);
        } catch (InterruptedException e) {
            new ExceptionView(new RouteException(e.getMessage()))
                    .renderText(writer);
        }
    }

}
