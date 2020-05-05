package pt.isel.ls.model.dsl.elements;

import pt.isel.ls.model.dsl.Node;

public class BodyElement extends Element {

    public BodyElement(Node... children) {
        super(children);
    }

    @Override
    protected String getNodeName() {
        return "body";
    }

}
