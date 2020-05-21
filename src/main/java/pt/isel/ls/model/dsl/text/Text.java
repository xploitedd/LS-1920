package pt.isel.ls.model.dsl.text;

public class Text extends TextNode {

    public Text(Object text) {
        super(text);
    }

    @Override
    protected String getNodeName() {
        return "";
    }

    @Override
    protected String getClosingTag() {
        return "";
    }

    @Override
    protected String getOpeningTag() {
        return "";
    }

}
