package pt.isel.ls.view.label;

import pt.isel.ls.handlers.label.PostLabelCreateHandler;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.form.HtmlFormBuilder;
import pt.isel.ls.view.utils.form.InputType;

import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.h1;

public class LabelCreateView extends View {

    private final String error;

    public LabelCreateView() {
        this(null);
    }

    public LabelCreateView(String error) {
        super("Create Label");
        this.error = error;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        Element form = new HtmlFormBuilder(Method.POST, handler.route(PostLabelCreateHandler.class))
                .withInput("name", "Label Name", InputType.TEXT, true)
                .withError(error)
                .build("Create Label");

        return div(
                h1("Create a new label"),
                form
        );
    }

}
