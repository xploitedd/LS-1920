package pt.isel.ls.dsl.elements;

import java.io.IOException;
import java.io.Writer;
import pt.isel.ls.dsl.Node;

public abstract class Element extends Node {

    protected Node[] children;

    public Element(Node... children) {
        this.children = children;
    }

    @Override
    public void write(Writer writer) throws IOException {
        writer.write(getOpeningTag());
        for (Node n : children) {
            n.write(writer);
        }

        writer.write(getClosingTag());
        writer.flush();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getOpeningTag());
        for (Node n : children) {
            sb.append(n.toString());
        }

        return sb.append(getClosingTag()).toString();
    }

}
