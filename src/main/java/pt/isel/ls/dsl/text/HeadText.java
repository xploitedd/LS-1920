package pt.isel.ls.dsl.text;

public class HeadText extends TextNode {

    private int headerLevel;

    public HeadText(int headerLevel, String text) {
        super(text);
        this.headerLevel = headerLevel;
    }

    @Override
    protected String getNodeName() {
        return "h" + headerLevel;
    }

}
