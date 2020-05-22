package pt.isel.ls.view;

import java.io.IOException;
import java.io.PrintWriter;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.misc.GetHomeHandler;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.body;
import static pt.isel.ls.model.dsl.Dsl.br;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.head;
import static pt.isel.ls.model.dsl.Dsl.html;
import static pt.isel.ls.model.dsl.Dsl.p;
import static pt.isel.ls.model.dsl.Dsl.title;

public abstract class View {

    protected final String title;
    private final boolean homeLink;

    /**
     * Creates a new View
     * @param title title of this view
     */
    public View(String title) {
        this(title, true);
    }

    /**
     * Creates a new View
     * @param title title of this view
     * @param homeLink specifies whether this view has a link to
     *                 the homepage in the HTML representation
     */
    public View(String title, boolean homeLink) {
        this.title = title;
        this.homeLink = homeLink;
    }

    /**
     * Render this view
     * @param handler view handler
     * @param type render type
     * @param writer writer where to render
     */
    final void render(ViewHandler handler, ViewType type, PrintWriter writer) {
        if (type == ViewType.HTML) {
            renderHtml(handler, writer);
        } else {
            renderText(handler, writer);
        }
    }

    /**
     * Render this view in HTML
     * @param handler view handler
     * @param writer HTML writer
     */
    private void renderHtml(ViewHandler handler, PrintWriter writer) {
        try {
            Element body;
            if (homeLink) {
                Node homeLink = a(handler.route(GetHomeHandler.class),
                        "Go to home");
                body = body(
                        div(homeLink),
                        br(),
                        getHtmlBody(handler)
                );
            } else {
                body = body(getHtmlBody(handler));
            }

            Element html = html(head(title(title)), body);
            html.write(writer);
            writer.println();
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    /**
     * Get the HTML body for this View
     * @param handler view handler
     * @return HTML body content
     */
    protected Node getHtmlBody(ViewHandler handler) {
        return p("No html representation available!");
    }

    /**
     * Render this View in plaintext
     * @param handler view handler
     * @param writer text writer
     */
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        writer.println("No text representation available!");
    }

    /**
     * Get the title of this view
     * @return view title
     */
    public String getTitle() {
        return title;
    }

}
