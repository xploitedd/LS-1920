package pt.isel.ls.dsl.elements.table;

import pt.isel.ls.dsl.elements.Element;

public class TableElement extends Element {

    public TableElement(TableRowElement... rows) {
        super(rows);
    }

    @Override
    protected String getNodeName() {
        return "table";
    }

}
