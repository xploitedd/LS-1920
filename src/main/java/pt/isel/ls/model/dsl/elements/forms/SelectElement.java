package pt.isel.ls.model.dsl.elements.forms;

import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.text.forms.OptionText;

public class SelectElement extends Element {

    public SelectElement(String name, OptionText... options) {
        this(name, false, options);
    }

    public SelectElement(String name, boolean multiple, OptionText... options) {
        super(options);
        attr("name", name);
        attr("multiple", multiple ? "1" : "0");
    }

    @Override
    protected String getNodeName() {
        return "select";
    }

}
