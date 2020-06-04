package pt.isel.ls.view.user;

import pt.isel.ls.handlers.user.GetUsersHandler;
import pt.isel.ls.handlers.user.PostUserCreateHandler;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.parameter.ParameterErrors;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.form.HtmlFormBuilder;
import pt.isel.ls.view.utils.form.InputType;

import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.h1;

public class UserCreateView extends View {

    private final ParameterErrors errors;

    public UserCreateView() {
        this(null);
    }

    public UserCreateView(ParameterErrors errors) {
        super("Create User");
        this.errors = errors;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        Element el = new HtmlFormBuilder(Method.POST, handler.route(PostUserCreateHandler.class))
                .withInput("name", "Name", InputType.TEXT, true)
                .withInput("email", "Email", InputType.EMAIL, true)
                .withErrors(errors)
                .build("Create User");

        return div(
                h1("Create a new user"),
                el
        );
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetUsersHandler.class), "Users");
    }

}
