package pt.isel.ls.dsl.elements;

public class HeadElement extends Element {

    public HeadElement(Element... children) {
        super(children);
    }

    @Override
    protected String getElementName() {
        return "head";
    }

}
