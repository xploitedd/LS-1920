package pt.isel.ls.dsl.elements;

public class HElement extends TextElement {

    private int headerLevel;

    public HElement(int headerLevel, String text) {
        super(text);
        this.headerLevel = headerLevel;
    }

    @Override
    protected String getElementName() {
        return "h" + headerLevel;
    }

}
