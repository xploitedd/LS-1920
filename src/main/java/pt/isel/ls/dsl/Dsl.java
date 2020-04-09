package pt.isel.ls.dsl;

import pt.isel.ls.dsl.elements.BodyElement;
import pt.isel.ls.dsl.elements.Element;
import pt.isel.ls.dsl.elements.HeadElement;
import pt.isel.ls.dsl.elements.HtmlElement;
import pt.isel.ls.dsl.elements.table.TableElement;
import pt.isel.ls.dsl.elements.table.TableRowElement;
import pt.isel.ls.dsl.text.HeadText;
import pt.isel.ls.dsl.text.ParagraphText;
import pt.isel.ls.dsl.text.TitleText;
import pt.isel.ls.dsl.text.table.TableDataText;
import pt.isel.ls.dsl.text.table.TableHeaderText;
import pt.isel.ls.dsl.text.table.TableText;

public class Dsl {

    public static HtmlElement html(Element... children) {
        return new HtmlElement(children);
    }

    public static HeadElement head(Node... children) {
        return new HeadElement(children);
    }

    public static BodyElement body(Node... children) {
        return new BodyElement(children);
    }

    public static TitleText title(String title) {
        return new TitleText(title);
    }

    public static HeadText h1(String header) {
        return new HeadText(1, header);
    }

    public static HeadText h2(String header) {
        return new HeadText(2, header);
    }

    public static HeadText h3(String header) {
        return new HeadText(3, header);
    }

    public static ParagraphText p(String paragraph) {
        return new ParagraphText(paragraph);
    }

    public static TableElement table(TableRowElement... rows) {
        return new TableElement(rows);
    }

    public static TableRowElement tr(TableText... tableTexts) {
        return new TableRowElement(tableTexts);
    }

    public static TableText th(String header) {
        return new TableHeaderText(header);
    }

    public static TableText td(String data) {
        return new TableDataText(data);
    }

}
