package pt.isel.ls.dsl.elements;

public abstract class TextElement extends Element {

    protected String text;

    public TextElement(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return getOpeningTag() + text + getClosingTag();
    }

}
