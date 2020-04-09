package pt.isel.ls.dsl;

import java.io.IOException;
import java.io.Writer;

public abstract class Node {

    protected abstract String getNodeName();

    protected String getOpeningTag() {
        return "<" + getNodeName() + ">";
    }

    protected String getClosingTag() {
        return "</" + getNodeName() + ">";
    }

    public abstract void write(Writer writer) throws IOException;

}
