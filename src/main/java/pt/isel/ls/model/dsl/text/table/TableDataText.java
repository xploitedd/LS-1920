package pt.isel.ls.model.dsl.text.table;

import pt.isel.ls.model.dsl.Node;

public class TableDataText extends TableText {

    public TableDataText(Object text) {
        super(text.toString());
    }

    public TableDataText(Node data) {
        super(data.toString());
    }

    @Override
    protected String getNodeName() {
        return "td";
    }

}
