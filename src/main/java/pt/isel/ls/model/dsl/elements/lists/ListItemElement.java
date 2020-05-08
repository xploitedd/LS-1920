package pt.isel.ls.model.dsl.elements.lists;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;

public class ListItemElement extends Element {

    public ListItemElement(Node... children) {
        super(children);
    }

    @Override
    protected String getNodeName() {
        return "li";
    }

}
