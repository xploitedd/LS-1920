package pt.isel.ls.view.console;

import java.io.PrintWriter;

public class MessageView implements View {

    private String message;

    public MessageView(String message) {
        this.message = message;
    }

    @Override
    public void render(PrintWriter writer) {
        writer.println("---------------------------------------------------------------------------");
        writer.println("\t" + message);
        writer.println("---------------------------------------------------------------------------");
    }
}
