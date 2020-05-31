package pt.isel.ls.model.dsl.elements;

public class LinkElement extends Element {

    public LinkElement(String rel, String type, String href) {
        attr("rel", rel);
        attr("type", type);
        attr("href", href);
    }

    @Override
    protected String getNodeName() {
        return "link";
    }

    @Override
    protected boolean canHaveChildren() {
        return false;
    }

}
