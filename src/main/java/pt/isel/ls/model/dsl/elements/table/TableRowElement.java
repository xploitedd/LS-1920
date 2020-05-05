package pt.isel.ls.model.dsl.elements.table;

import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.text.table.TableText;

public class TableRowElement extends Element {

    public TableRowElement(TableText... tableTexts) {
        super(tableTexts);
    }

    @Override
    protected String getNodeName() {
        return "tr";
    }

}
