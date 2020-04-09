package pt.isel.ls.dsl;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;
import pt.isel.ls.dsl.elements.Element;

import static org.junit.Assert.assertEquals;
import static pt.isel.ls.dsl.Dsl.body;
import static pt.isel.ls.dsl.Dsl.h1;
import static pt.isel.ls.dsl.Dsl.h2;
import static pt.isel.ls.dsl.Dsl.head;
import static pt.isel.ls.dsl.Dsl.html;
import static pt.isel.ls.dsl.Dsl.title;

public class DslTest {

    @Test
    public void testResultingHtml() throws IOException {
        Element html =
                html(
                        head(
                                title("Hello World!")
                        ),
                        body(
                                h1("Hello World!"),
                                h2("This is a subtitle")
                        )
                );

        String parsedHtml = "<html><head><title>Hello World!</title></head>"
                + "<body><h1>Hello World!</h1><h2>This is a subtitle</h2></body></html>";

        StringWriter writer = new StringWriter();
        html.write(writer);
        assertEquals(parsedHtml, writer.toString());
    }

}
