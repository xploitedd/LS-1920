package pt.isel.ls.view.label;

import pt.isel.ls.handlers.label.GetLabelsHandler;
import pt.isel.ls.handlers.label.PostLabelCreateHandler;
import pt.isel.ls.model.dsl.elements.forms.FormElement;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.error.HandlerError;
import pt.isel.ls.router.response.error.ParameterError;
import pt.isel.ls.view.FormView;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.form.HtmlFormBuilder;
import pt.isel.ls.view.utils.form.HtmlFormInput;
import pt.isel.ls.view.utils.form.InputType;

public class LabelCreateView extends FormView {

    public LabelCreateView() {
        super("Create a new label");
    }

    public LabelCreateView(ParameterError errors, RouteRequest request) {
        super("Create a new label", errors, request);
    }

    public LabelCreateView(HandlerError errors) {
        super("Create a new label", errors);
    }

    @Override
    protected FormElement getForm(ViewHandler handler) {
        return new HtmlFormBuilder(Method.POST, handler.route(PostLabelCreateHandler.class))
                .withInput(new HtmlFormInput("name", "Label Name", InputType.TEXT, true))
                .withErrors(parameterErrors, request)
                .build("Create Label");
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetLabelsHandler.class), "Labels");
    }

}
