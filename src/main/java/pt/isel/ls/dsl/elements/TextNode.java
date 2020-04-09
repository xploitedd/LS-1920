package pt.isel.ls.dsl.elements;

import java.io.IOException;
import java.io.Writer;

public abstract class TextNode extends Node {

    protected String text;

    public TextNode(String text) {
        this.text = text;
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

}
