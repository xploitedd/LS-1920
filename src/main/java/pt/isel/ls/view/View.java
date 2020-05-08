package pt.isel.ls.view;

import java.io.IOException;
import java.io.PrintWriter;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.router.Router;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.body;
import static pt.isel.ls.model.dsl.Dsl.head;
import static pt.isel.ls.model.dsl.Dsl.html;
import static pt.isel.ls.model.dsl.Dsl.p;
import static pt.isel.ls.model.dsl.Dsl.title;

public abstract class View {

    protected final String title;
    private final boolean homeLink;

    public View(String title) {
        this(title, true);
    }

    public View(String title, boolean homeLink) {
        this.title = title;
        this.homeLink = homeLink;
    }

    public final void render(Router router, ViewType type, PrintWriter writer) {
        if (type == ViewType.HTML) {
            renderHtml(router, writer);
        } else {
            renderText(writer);
        }
    }

    private void renderHtml(Router router, PrintWriter writer) {
        try {
            Element body = body(getHtmlBody(router));
            if (homeLink) {
                Node homeLink = a(router.routeFromName("GetHomeHandler"),
                        "Go to home");
                body = body(homeLink, getHtmlBody(router));
            }

            Element html = html(head(title(title)), body);
            html.write(writer);
            writer.println();
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    protected Node getHtmlBody(Router router) {
        return p("No html representation available!");
    }

    protected void renderText(PrintWriter writer) {
        writer.println("No text representation available!");
    }

    public String getTitle() {
        return title;
    }

}
