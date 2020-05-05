package pt.isel.ls.model.dsl.elements;

public class HtmlElement extends Element {

    private static final String HTML_HEADER = "<!DOCTYPE html>";

    public HtmlElement(Element... children) {
        super(children);
    }

    @Override
    protected String getNodeName() {
        return "html";
    }

    @Override
    protected String getOpeningTag() {
        return HTML_HEADER + super.getOpeningTag();
    }
}
