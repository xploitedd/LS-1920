package pt.isel.ls.model.dsl.elements;

import pt.isel.ls.model.dsl.Node;

public class HeadElement extends Element {

    public HeadElement(Node... children) {
        super(children);
    }

    @Override
    protected String getNodeName() {
        return "head";
    }

}
