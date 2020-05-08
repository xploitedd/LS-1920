package pt.isel.ls.model.dsl.elements;

import pt.isel.ls.model.dsl.Node;

public class DivElement extends Element {

    public DivElement(Node... children) {
        super(children);
    }

    @Override
    protected String getNodeName() {
        return "div";
    }

}
