package pt.isel.ls.view;

import java.io.IOException;
import java.io.PrintWriter;
import pt.isel.ls.dsl.Node;
import pt.isel.ls.dsl.elements.Element;

import static pt.isel.ls.dsl.Dsl.body;
import static pt.isel.ls.dsl.Dsl.head;
import static pt.isel.ls.dsl.Dsl.html;
import static pt.isel.ls.dsl.Dsl.p;
import static pt.isel.ls.dsl.Dsl.title;

public abstract class View {

    /**
     * Renders this view on the selected writer
     * @param type render type
     * @param writer writer where to render this view
     */
    public final void render(ViewType type, PrintWriter writer) {
        if (type == ViewType.HTML) {
            try {
                Element html =
                        html(
                                head(title("Placeholder Title")),
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

}
