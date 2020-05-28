package pt.isel.ls.view.user;

import pt.isel.ls.handlers.user.PostUserCreateHandler;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.button;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.form;
import static pt.isel.ls.model.dsl.Dsl.input;
import static pt.isel.ls.model.dsl.Dsl.label;

public class UserCreateView extends View {

    public UserCreateView() {
        super("Create User");
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        return form(
                "post",
                handler.route(PostUserCreateHandler.class),
                div(
                        label("uname", "UserName"),
                        input("text","name")
                                .attr("id","uname")
                ),
                div(
                        label("mail", "Email"),
                        input("text", "email")
                                .attr("id", "mail")
                ),
                div(
                        button("Create")
                                .attr("type", "submit")
                )
        );
    }
}
