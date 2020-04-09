package pt.isel.ls.dsl;

import pt.isel.ls.dsl.elements.BodyElement;
import pt.isel.ls.dsl.elements.Element;
import pt.isel.ls.dsl.elements.HElement;
import pt.isel.ls.dsl.elements.HeadElement;
import pt.isel.ls.dsl.elements.HtmlElement;
import pt.isel.ls.dsl.elements.Node;
import pt.isel.ls.dsl.elements.TitleElement;

public class Dsl {

    public static Element html(Element... children) {
        return new HtmlElement(children);
    }

    public static Element head(Node... children) {
        return new HeadElement(children);
    }

    public static Element body(Node... children) {
        return new BodyElement(children);
    }

    public static Node title(String title) {
        return new TitleElement(title);
    }

    public static Node h1(String header) {
        return new HElement(1, header);
    }

    public static Node h2(String header) {
        return new HElement(2, header);
    }

    public static Node h3(String header) {
        return new HElement(3, header);
    }

}
