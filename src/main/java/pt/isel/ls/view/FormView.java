package pt.isel.ls.view;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.forms.FormElement;
import pt.isel.ls.router.response.error.HandlerErrors;
import pt.isel.ls.router.response.error.ParameterErrors;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.h1;

public abstract class FormView extends View {

    private HandlerErrors handlerErrors;
    protected ParameterErrors parameterErrors;

    public FormView(String title) {
        super(title);
    }

    public FormView(String title, HandlerErrors handlerErrors) {
        super(title);
        this.handlerErrors = handlerErrors;
    }

    public FormView(String title, ParameterErrors parameterErrors) {
        super(title);
        this.parameterErrors = parameterErrors;
    }

    protected abstract FormElement getForm(ViewHandler handler);

    @Override
    protected final Node getHtmlBody(ViewHandler handler) {
        FormElement form = getForm(handler);
        if (handlerErrors != null) {
            return div(
                    h1(title),
                    handlerErrors.toHtml(),
                    form
            );
        }

        return div(
                h1(title),
                form
        );
    }

    @Override
    protected final void renderText(ViewHandler handler, PrintWriter writer) {
        super.renderText(handler, writer);
    }

}
