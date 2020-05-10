package pt.isel.ls.model.dsl.text.forms;

import pt.isel.ls.model.dsl.text.TextNode;

public class OptionText extends TextNode {

    public OptionText(String value, String text) {
        super(text);
        attr("value", value);
    }

    @Override
    protected String getNodeName() {
        return "option";
    }

}
