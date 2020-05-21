package pt.isel.ls.model.dsl.text;

public class LabelText extends TextNode {

    public LabelText(String forId, Object text) {
        super(text);
        attr("for", forId);
    }

    @Override
    protected String getNodeName() {
        return "label";
    }

}
