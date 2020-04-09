package pt.isel.ls.dsl.elements;

public class BodyElement extends Element {

    public BodyElement(Element... children) {
        super(children);
    }

    @Override
    protected String getElementName() {
        return "body";
    }

}
