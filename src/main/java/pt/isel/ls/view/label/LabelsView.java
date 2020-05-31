package pt.isel.ls.view.label;

import pt.isel.ls.handlers.label.GetLabelCreateHandler;
import pt.isel.ls.handlers.label.GetLabelHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.table.HtmlTableBuilder;
import pt.isel.ls.view.utils.table.StringTableBuilder;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class LabelsView extends View {

    private final Iterable<Label> labels;

    public LabelsView(Iterable<Label> labels) {
        super("Labels");
        this.labels = labels;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        addNavEntry(a(handler.route(GetLabelCreateHandler.class), "Create Label"));
        Element el = new HtmlTableBuilder<>(labels)
                .withColumn("Id", Label::getLid)
                .withColumn("Name", Label::getName)
                .withColumn("Label Link", l -> a(
                        handler.route(GetLabelHandler.class, l.getLid()),
                        "Show details"
                ))
                .build();

        return div(el);
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        writer.write(new StringTableBuilder<>(labels)
                .withColumn("Id", Label::getLid)
                .withColumn("Name", Label::getName)
                .build());
    }

}
