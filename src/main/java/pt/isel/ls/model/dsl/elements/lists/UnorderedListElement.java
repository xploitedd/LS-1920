package pt.isel.ls.model.dsl.elements.lists;

import pt.isel.ls.model.dsl.elements.Element;

public class UnorderedListElement extends Element {

    public UnorderedListElement(ListItemElement... items) {
        super(items);
    }

    @Override
    protected String getNodeName() {
        return "ul";
    }

}
