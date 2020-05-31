package pt.isel.ls.model.dsl.elements.forms;

import pt.isel.ls.model.dsl.elements.Element;

public class InputElement extends Element {

    public InputElement(String type, String name) {
        this(type, name, false);
    }

    public InputElement(String type, String name, boolean required) {
        attr("type", type);
        attr("name", name);
        attr("required", required ? "1" : "0");
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
