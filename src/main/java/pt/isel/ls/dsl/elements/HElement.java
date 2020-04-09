package pt.isel.ls.dsl.elements;

public class HElement extends TextNode {

    private int headerLevel;

    public HElement(int headerLevel, String text) {
        super(text);
        this.headerLevel = headerLevel;
    }

    @Override
    protected String getNodeName() {
        return "h" + headerLevel;
    }

}
