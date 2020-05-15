package pt.isel.ls.view.label;

import pt.isel.ls.handlers.label.GetLabelsHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.TableView;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class LabelView extends TableView {

    public LabelView(String tableName, Table table) {
        super(tableName, table);
    }

    protected Node getHtmlBody(ViewHandler handler) {
        return div(
                super.getHtmlBody(handler),
                a(handler.route(GetLabelsHandler.class), "Labels")
        );
    }

}
