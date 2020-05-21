package pt.isel.ls.model.dsl.text;

public class ParagraphText extends TextNode {

    public ParagraphText(Object text) {
        super(text);
    }

    @Override
    protected String getNodeName() {
        return "p";
    }

}
