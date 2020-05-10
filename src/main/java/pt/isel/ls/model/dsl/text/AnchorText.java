package pt.isel.ls.model.dsl.text;

public class AnchorText extends TextNode {

    public AnchorText(String href, String text) {
        super(text);
        attr("href", href);
    }

    @Override
    protected String getNodeName() {
        return "a";
    }

}
