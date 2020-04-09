package pt.isel.ls.dsl.elements;

import pt.isel.ls.dsl.Node;

public class HeadElement extends Element {

    public HeadElement(Node... children) {
        super(children);
    }

    @Override
    protected String getNodeName() {
        return "head";
    }

}
