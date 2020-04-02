package pt.isel.ls.view;

import java.io.PrintWriter;

public abstract class View {

    /**
     * Renders this view on the selected writer
     * @param type render type
     * @param writer writer where to render this view
     */
    public final void render(ViewType type, PrintWriter writer) {
        if (type == ViewType.HTML) {
            renderHtml(writer);
        } else {
            renderText(writer);
        }
    }

    protected void renderHtml(PrintWriter writer) {
        // by default renderHtml renders in text mode
        renderText(writer);
    }

    protected abstract void renderText(PrintWriter writer);

}
