package pt.isel.ls.view;

import java.io.PrintWriter;
import pt.isel.ls.router.response.RouteException;

public final class ExitView extends View {

    @Override
    public void renderText(PrintWriter writer) {
        writer.println("Closing Application. Bye!");
        writer.close();
        try {
            Thread.sleep(2000);
            System.exit(0);
        } catch (InterruptedException e) {
            new RouteExceptionView(new RouteException(e.getMessage()))
                    .renderText(writer);
        }
    }

}
