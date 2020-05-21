package pt.isel.ls.model.dsl.text;

public class HeadText extends TextNode {

    private final int headerLevel;

    public HeadText(int headerLevel, Object text) {
        super(text);
        this.headerLevel = headerLevel;
    }

    @Override
    protected String getNodeName() {
        return "h" + headerLevel;
    }

}
