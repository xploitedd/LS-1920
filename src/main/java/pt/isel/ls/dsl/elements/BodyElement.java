package pt.isel.ls.dsl.elements;

import pt.isel.ls.dsl.Node;

public class BodyElement extends Element {

    public BodyElement(Node... children) {
        super(children);
    }

    @Override
    protected String getNodeName() {
        return "body";
    }

}
