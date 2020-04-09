package pt.isel.ls.dsl.elements;

public class TitleElement extends TextElement {

    public TitleElement(String text) {
        super(text);
    }

    @Override
    protected String getElementName() {
        return "title";
    }

}
