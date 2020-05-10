package pt.isel.ls.model.dsl.text.forms;

import pt.isel.ls.model.dsl.text.TextNode;

public class LabelText extends TextNode {

    public LabelText(String forId, String text) {
        super(text);
        attr("for", forId);
    }

    @Override
    protected String getNodeName() {
        return "label";
    }

}
