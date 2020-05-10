package pt.isel.ls.model.dsl.elements.forms;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;

public class FormElement extends Element {

    public FormElement(String method, String action, Node... children) {
        super(children);
        attr("method", method);
        attr("action", action);
    }

    @Override
    protected String getNodeName() {
        return "form";
    }

}
