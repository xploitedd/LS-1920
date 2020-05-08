package pt.isel.ls.model.dsl.text;

public class AnchorText extends TextNode {

    private final String href;

    public AnchorText(String href, String text) {
        super(text);
        this.href = href;
    }

    @Override
    protected String getNodeName() {
        return "a";
    }

    @Override
    protected String getOpeningTag() {
        String tag = super.getOpeningTag();
        tag = tag.substring(0, tag.length() - 1);
        return tag + " href=\"" + href + "\">";
    }
}
