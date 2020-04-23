package pt.isel.ls.dsl;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;

public abstract class Node {

    private final HashSet<Attribute> attributes = new HashSet<>();

    protected abstract String getNodeName();

    protected String getOpeningTag() {
        StringBuilder sb = new StringBuilder();
        for (Attribute a : attributes) {
            sb.append(" ").append(a);
        }

        return "<" + getNodeName() + sb.toString() + ">";
    }

    protected String getClosingTag() {
        return "</" + getNodeName() + ">";
    }

    public abstract void write(Writer writer) throws IOException;

    @SuppressWarnings("unchecked")
    public <T extends Node> T addAttribute(String name, String value) {
        attributes.add(new Attribute(name, value));
        return (T) this;
    }

}
