package pt.isel.ls.view;

import java.io.IOException;
import java.io.PrintWriter;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;

import static pt.isel.ls.model.dsl.Dsl.body;
import static pt.isel.ls.model.dsl.Dsl.head;
import static pt.isel.ls.model.dsl.Dsl.html;
import static pt.isel.ls.model.dsl.Dsl.p;
import static pt.isel.ls.model.dsl.Dsl.title;

public abstract class View {

    protected final String title;

    public View(String title) {
        this.title = title;
    }

    /**
     * Renders this view on the selected writer
     * @param type render type
     * @param writer writer where to render this view
     * @return the type of the response
     */
    public final void render(ViewType type, PrintWriter writer) {
        if (type == ViewType.HTML) {
            try {
                Element html =
                        html(
                                head(title(title)),
                                body(getHtmlBody())
                        );

                html.write(writer);
                writer.println();
            } catch (IOException e) {
                renderText(writer);
            }
        } else {
            renderText(writer);
        }
    }

    protected Node getHtmlBody() {
        return p("No html representation available!");
    }

    protected abstract void renderText(PrintWriter writer);

    public String getTitle() {
        return title;
    }

}
