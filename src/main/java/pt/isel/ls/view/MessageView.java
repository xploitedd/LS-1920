package pt.isel.ls.view;

import java.io.PrintWriter;

public class MessageView extends View {

    public MessageView(String message) {
        super(message);
    }

    @Override
    protected void renderText(PrintWriter writer) {
        writer.println("---------------------------------------------------------------------------");
        writer.println("\t" + title);
        writer.println("---------------------------------------------------------------------------");
    }

}
