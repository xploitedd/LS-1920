package pt.isel.ls.model.dsl.text;

import java.io.IOException;
import java.io.Writer;
import pt.isel.ls.model.dsl.Node;

public abstract class TextNode extends Node {

    protected String text;

    public TextNode(Object text) {
        this.text = text.toString();
    }

    @Override
    protected final boolean canHaveChildren() {
        return true;
    }

    @Override
    public void write(Writer writer) throws IOException {
        writer.write(getOpeningTag());
        writer.write(text);
        writer.write(getClosingTag());
        writer.flush();
    }

    @Override
    public String toString() {
        return getOpeningTag() + text + getClosingTag();
    }

    public String getText() {
        return text;
    }

}
