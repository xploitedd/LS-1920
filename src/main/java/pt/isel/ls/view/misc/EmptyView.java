package pt.isel.ls.view.misc;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import java.io.PrintWriter;

public class EmptyView extends View {

    public EmptyView() {
        super("");
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {

    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        return null;
    }

}
