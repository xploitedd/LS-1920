package pt.isel.ls.model.dsl.elements;

public class BreakElement extends Element {

    @Override
    protected boolean canHaveChildren() {
        return false;
    }

    @Override
    protected String getNodeName() {
        return "br";
    }

}
