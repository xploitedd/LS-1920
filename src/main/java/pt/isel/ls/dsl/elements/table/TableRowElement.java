package pt.isel.ls.dsl.elements.table;

import pt.isel.ls.dsl.elements.Element;
import pt.isel.ls.dsl.text.table.TableText;

public class TableRowElement extends Element {

    public TableRowElement(TableText... tableTexts) {
        super(tableTexts);
    }

    @Override
    protected String getNodeName() {
        return "tr";
    }

}
