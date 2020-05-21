package pt.isel.ls.view.user;

import pt.isel.ls.handlers.user.GetUsersHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class UserView extends View {

    public UserView(String tableName, Table table) {
        super(tableName);
    }

    protected Node getHtmlBody(ViewHandler handler) {
        return div(
                super.getHtmlBody(handler),
                a(handler.route(GetUsersHandler.class), "Users")
        );
    }

}
