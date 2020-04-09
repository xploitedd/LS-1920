package pt.isel.ls.dsl.elements;

public class HtmlElement extends Element {

    public HtmlElement(Element... children) {
        super(children);
    }

    @Override
    protected String getElementName() {
        return "html";
    }

}
