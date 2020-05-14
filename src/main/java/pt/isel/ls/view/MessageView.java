package pt.isel.ls.view;

import pt.isel.ls.model.dsl.Node;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.h1;

public class MessageView extends View {

    public MessageView(String message) {
        super(message);
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        writer.println(
                "---------------------------------------------------------------------------\n\t"
                + title
                + "\n---------------------------------------------------------------------------"
        );
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        return h1(title);
    }

}
