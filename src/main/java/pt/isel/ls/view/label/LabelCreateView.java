package pt.isel.ls.view.label;

import pt.isel.ls.handlers.label.PostLabelCreateHandler;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.button;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.form;
import static pt.isel.ls.model.dsl.Dsl.input;
import static pt.isel.ls.model.dsl.Dsl.label;

public class LabelCreateView extends View {
    public LabelCreateView() {
        super("Create Label");
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        return form(
                "post",
                handler.route(PostLabelCreateHandler.class),
                div(
                        label("name", "Label Name"),
                        input("text","name")
                                .attr("id","name")
                ),
                div(
                        button("Create")
                                .attr("type", "submit")
                )
        );
    }
}
