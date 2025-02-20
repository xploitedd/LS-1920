package pt.isel.ls.model.dsl;

import pt.isel.ls.model.dsl.elements.BodyElement;
import pt.isel.ls.model.dsl.elements.BreakElement;
import pt.isel.ls.model.dsl.elements.DivElement;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.elements.HeadElement;
import pt.isel.ls.model.dsl.elements.HtmlElement;
import pt.isel.ls.model.dsl.elements.LinkElement;
import pt.isel.ls.model.dsl.elements.NavElement;
import pt.isel.ls.model.dsl.elements.forms.FormElement;
import pt.isel.ls.model.dsl.elements.forms.InputElement;
import pt.isel.ls.model.dsl.elements.forms.SelectElement;
import pt.isel.ls.model.dsl.elements.lists.ListItemElement;
import pt.isel.ls.model.dsl.elements.lists.UnorderedListElement;
import pt.isel.ls.model.dsl.elements.table.TableElement;
import pt.isel.ls.model.dsl.elements.table.TableRowElement;
import pt.isel.ls.model.dsl.text.AnchorText;
import pt.isel.ls.model.dsl.text.BoldText;
import pt.isel.ls.model.dsl.text.ButtonText;
import pt.isel.ls.model.dsl.text.HeadText;
import pt.isel.ls.model.dsl.text.LabelText;
import pt.isel.ls.model.dsl.text.ParagraphText;
import pt.isel.ls.model.dsl.text.Text;
import pt.isel.ls.model.dsl.text.TitleText;
import pt.isel.ls.model.dsl.text.forms.OptionText;
import pt.isel.ls.model.dsl.text.table.TableDataText;
import pt.isel.ls.model.dsl.text.table.TableHeaderText;
import pt.isel.ls.model.dsl.text.table.TableText;

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

    public static HeadText h1(Object header) {
        return new HeadText(1, header);
    }

    public static HeadText h2(Object header) {
        return new HeadText(2, header);
    }

    public static HeadText h3(Object header) {
        return new HeadText(3, header);
    }

    public static ParagraphText p(Object paragraph) {
        return new ParagraphText(paragraph);
    }

    public static TableElement table(TableRowElement... rows) {
        return new TableElement(rows);
    }

    public static TableRowElement tr(TableText... tableTexts) {
        return new TableRowElement(tableTexts);
    }

    public static TableHeaderText th(String header) {
        return new TableHeaderText(header);
    }

    public static TableDataText td(Object data) {
        return new TableDataText(data);
    }

    public static UnorderedListElement ul(ListItemElement... items) {
        return new UnorderedListElement(items);
    }

    public static ListItemElement li(Node... children) {
        return new ListItemElement(children);
    }

    public static DivElement div(Node... children) {
        return new DivElement(children);
    }

    public static AnchorText a(String href, String text) {
        return new AnchorText(href, text);
    }

    public static FormElement form(String method, String action, Node... children) {
        return new FormElement(method, action, children);
    }

    public static InputElement input(String type, String name) {
        return new InputElement(type, name);
    }

    public static InputElement input(String type, String name, boolean required) {
        return new InputElement(type, name, required);
    }

    public static SelectElement select(String name, OptionText... options) {
        return new SelectElement(name, options);
    }

    public static SelectElement select(String name, boolean multiple, OptionText... options) {
        return new SelectElement(name, multiple, options);
    }

    public static OptionText option(String value, String text) {
        return new OptionText(value, text);
    }

    public static LabelText label(String forId, Object text) {
        return new LabelText(forId, text);
    }

    public static ButtonText button(String text) {
        return new ButtonText(text);
    }

    public static BreakElement br() {
        return new BreakElement();
    }

    public static BoldText b(Object text) {
        return new BoldText(text);
    }

    public static Text text(Object text) {
        return new Text(text);
    }

    public static LinkElement link(String ref, String type, String href) {
        return new LinkElement(ref, type, href);
    }

    public static NavElement nav(Node... children) {
        return new NavElement(children);
    }

}
