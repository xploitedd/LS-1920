package pt.isel.ls.view;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.router.Router;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.h1;

public class MessageView extends View {

    public MessageView(String message) {
        super(message);
    }

    @Override
    protected void renderText(PrintWriter writer) {
        writer.println(
                "---------------------------------------------------------------------------\n\t"
                + title
                + "\n---------------------------------------------------------------------------"
        );
    }

    @Override
    protected Node getHtmlBody(Router router) {
        return h1(title);
    }

}
