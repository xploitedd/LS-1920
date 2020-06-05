package pt.isel.ls.view.user;

import pt.isel.ls.handlers.user.GetUsersHandler;
import pt.isel.ls.handlers.user.PostUserCreateHandler;
import pt.isel.ls.model.dsl.elements.forms.FormElement;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.response.error.HandlerError;
import pt.isel.ls.router.response.error.ParameterError;
import pt.isel.ls.view.FormView;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.form.HtmlFormBuilder;
import pt.isel.ls.view.utils.form.HtmlFormInput;
import pt.isel.ls.view.utils.form.InputType;

public class UserCreateView extends FormView {

    public UserCreateView() {
        super("Create a new user");
    }

    public UserCreateView(ParameterError errors) {
        super("Create a new user", errors);
    }

    public UserCreateView(HandlerError errors) {
        super("Create a new user", errors);
    }

    @Override
    protected FormElement getForm(ViewHandler handler) {
        return new HtmlFormBuilder(Method.POST, handler.route(PostUserCreateHandler.class))
                .withInput(new HtmlFormInput("name", "Name", InputType.TEXT, true))
                .withInput(new HtmlFormInput("email", "Email", InputType.EMAIL, true))
                .withErrors(parameterErrors)
                .build("Create User");
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetUsersHandler.class), "Users");
    }

}
