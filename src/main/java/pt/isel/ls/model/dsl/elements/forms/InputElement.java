package pt.isel.ls.model.dsl.elements.forms;

import pt.isel.ls.model.dsl.elements.Element;

public class InputElement extends Element {

    public InputElement(String type, String name) {
        attr("type", type);
        attr("name", name);
    }

    @Override
    protected String getNodeName() {
        return "input";
    }

    @Override
    protected boolean canHaveChildren() {
        return false;
    }
}
