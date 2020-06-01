package pt.isel.ls.view.user;

import pt.isel.ls.handlers.user.GetUserCreateHandler;
import pt.isel.ls.handlers.user.GetUserHandler;
import pt.isel.ls.model.User;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.table.HtmlTableBuilder;
import pt.isel.ls.view.utils.table.StringTableBuilder;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class UsersView extends View {

    private final Iterable<User> users;

    public UsersView(Iterable<User> users) {
        super("Users");
        this.users = users;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        Element el = new HtmlTableBuilder<>(users)
                .withColumn("Id", User::getUid)
                .withColumn("Name", User::getName)
                .withColumn("Email", User::getEmail)
                .withColumn("User Link", u -> a(
                        handler.route(GetUserHandler.class, u.getUid()),
                        "Show details"
                ))
                .build();

        return div(el);
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        writer.write(new StringTableBuilder<>(users)
                .withColumn("Id", User::getUid)
                .withColumn("Name", User::getName)
                .withColumn("Email", User::getEmail)
                .build());
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetUserCreateHandler.class), "Create User");
    }

}
