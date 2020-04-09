package pt.isel.ls.dsl.elements;

public class BodyElement extends Element {

    public BodyElement(Node... children) {
        super(children);
    }

    @Override
    protected String getNodeName() {
        return "body";
    }

}
