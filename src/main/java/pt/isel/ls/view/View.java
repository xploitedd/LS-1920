package pt.isel.ls.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.elements.NavElement;
import pt.isel.ls.model.dsl.text.AnchorText;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.body;
import static pt.isel.ls.model.dsl.Dsl.head;
import static pt.isel.ls.model.dsl.Dsl.html;
import static pt.isel.ls.model.dsl.Dsl.link;
import static pt.isel.ls.model.dsl.Dsl.nav;
import static pt.isel.ls.model.dsl.Dsl.p;
import static pt.isel.ls.model.dsl.Dsl.title;

public abstract class View {

    private static final String HOME_LINK = "/";
    private static final String CSS_HREF = "/assets/css/style.css";

    private final ArrayList<AnchorText> navEntries = new ArrayList<>();
    protected final String title;

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

        if (homeLink) {
            navEntries.add(a(HOME_LINK, "Go to home"));
        }
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
            registerNavLinks(handler);
            Element html = html(
                    head(
                            title(title),
                            link("stylesheet", "text/css", CSS_HREF)
                    ),
                    body(
                            getNav(),
                            getHtmlBody(handler)
                    )
            );

            html.write(writer);
            writer.println();
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    private NavElement getNav() {
        if (navEntries.size() != 0) {
            return nav(navEntries.toArray(Node[]::new));
        }

        return null;
    }

    protected void addNavEntry(String href, String name) {
        navEntries.add(a(href, name));
    }

    protected void registerNavLinks(ViewHandler handler) {

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
