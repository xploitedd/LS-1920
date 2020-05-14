package pt.isel.ls.view.user;

import pt.isel.ls.model.Table;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.TableView;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class UserView extends TableView {

    public UserView(String tableName, Table table) {
        super(tableName, table);
    }

    protected Node getHtmlBody(ViewHandler handler) {
        return div(super.getHtmlBody(handler), a(handler.route("GetUsersHandler"),"Users"));
    }

}
