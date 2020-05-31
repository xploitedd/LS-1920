package pt.isel.ls.model.dsl.elements;

import pt.isel.ls.model.dsl.Node;

public class NavElement extends Element {

    public NavElement(Node... children) {
        super(children);
    }

    @Override
    protected String getNodeName() {
        return "nav";
    }

}
