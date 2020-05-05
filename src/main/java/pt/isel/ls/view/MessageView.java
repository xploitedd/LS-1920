package pt.isel.ls.view;

import pt.isel.ls.model.dsl.Node;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.h1;

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

    @Override
    protected Node getHtmlBody() {
        return h1(title);
    }
}
