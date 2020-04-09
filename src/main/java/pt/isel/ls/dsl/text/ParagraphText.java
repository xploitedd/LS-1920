package pt.isel.ls.dsl.text;

public class ParagraphText extends TextNode {

    public ParagraphText(String text) {
        super(text);
    }

    @Override
    protected String getNodeName() {
        return "p";
    }

}
