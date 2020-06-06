package pt.isel.ls.model.dsl;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public abstract class Node {

    private final HashMap<String, String> attributes = new HashMap<>();

    /**
     * Get the HTML name of this node
     * @return HTML name of the node
     */
    protected abstract String getNodeName();

    /**
     * Get the HTML closing tag for the node
     * @return closing tag of the node
     */
    protected String getClosingTag() {
        if (canHaveChildren()) {
            return "</" + getNodeName() + ">";
        }

        return "/>";
    }

    protected boolean canHaveChildren() {
        return true;
    }

    /**
     * Get the HTML opening tag for the node
     * @return opening tag of the node
     */
    protected String getOpeningTag() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(" ")
                    .append(key)
                    .append("=\"")
                    .append(value)
                    .append("\"");
        }

        return "<" + getNodeName() + sb.toString() + (canHaveChildren() ? ">" : "");
    }

    /**
     * Writes this node to a specified Writer
     * @param writer Writer where the node is going to be written
     * @throws IOException an IOException of the write operation
     */
    public abstract void write(Writer writer) throws IOException;

    /**
     * Adds a new HTML attribute to this node
     * @param name Attribute name
     * @param value Attribute value
     * @param <T> Generic type referring to the Node type
     * @return the Node, casted to its subtype
     */
    @SuppressWarnings("unchecked")
    public <T extends Node> T attr(String name, String value) {
        attributes.put(name, value);
        return (T) this;
    }

    public String getAttr(String name) {
        return attributes.get(name);
    }

}
