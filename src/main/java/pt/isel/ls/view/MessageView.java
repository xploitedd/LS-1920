package pt.isel.ls.view;

import java.io.PrintWriter;

public class MessageView extends View {

    private String message;

    public MessageView(String message) {
        this.message = message;
    }

    @Override
    public void renderText(PrintWriter writer) {
        writer.println("---------------------------------------------------------------------------");
        writer.println("\t" + message);
        writer.println("---------------------------------------------------------------------------");
    }

}
