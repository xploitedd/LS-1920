package pt.isel.ls.dsl.elements;

public abstract class Element {

    protected Element[] children;

    public Element(Element... children) {
        this.children = children;
    }

    protected abstract String getElementName();

    protected String getOpeningTag() {
        return "<" + getElementName() + ">";
    }

    protected String getClosingTag() {
        return "</" + getElementName() + ">";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getOpeningTag());
        for (Element c : children) {
            sb.append(c.toString());
        }

        return sb.append(getClosingTag()).toString();
    }

}
