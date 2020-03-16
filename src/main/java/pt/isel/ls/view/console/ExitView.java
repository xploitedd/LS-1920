package pt.isel.ls.view.console;

import java.io.PrintWriter;

public class ExitView implements View {

    @Override
    public void render(PrintWriter writer) {
        writer.println("Closing Application. Bye!");
        try {
            Thread.sleep(2000);
            System.exit(0);
        } catch (InterruptedException e) {
            new ThrowableView(e).render(writer);
        }
    }

}
