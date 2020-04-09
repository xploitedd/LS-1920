package pt.isel.ls.dsl.elements;

public class HeadElement extends Element {

    public HeadElement(Node... children) {
        super(children);
    }

    @Override
    protected String getNodeName() {
        return "head";
    }

}
