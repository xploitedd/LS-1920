package pt.isel.ls.dsl.elements;

public class TitleElement extends TextNode {

    public TitleElement(String text) {
        super(text);
    }

    @Override
    protected String getNodeName() {
        return "title";
    }

}
