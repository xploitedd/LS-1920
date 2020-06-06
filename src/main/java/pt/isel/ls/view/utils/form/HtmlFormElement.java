package pt.isel.ls.view.utils.form;

import pt.isel.ls.model.dsl.Dsl;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.text.LabelText;

import java.util.LinkedList;

import static pt.isel.ls.model.dsl.Dsl.div;

public abstract class HtmlFormElement {

    private final LinkedList<String> errors = new LinkedList<>();
    private final String name;
    private final LabelText label;
    protected final Element input;

    public HtmlFormElement(String name, String description, Element input) {
        this.name = name;
        this.label = Dsl.label(name, description);
        this.input = input.attr("id", name);
    }

    public String getName() {
        return name;
    }

    public HtmlFormElement withAttr(String attr, String value) {
        input.attr(attr, value);
        return this;
    }

    public HtmlFormElement withError(String error) {
        errors.add(error);
        return this;
    }

    public HtmlFormElement setValue(String value) {
        return withAttr("value", value);
    }

    public Node getContainer() {
        if (errors.size() != 0) {
            Node[] errs = errors.stream()
                    .map(Dsl::p)
                    .toArray(Node[]::new);

            return div(
                    div(errs).attr("class", "input-errors"),
                    label,
                    input
            ).attr("class", "form-input");
        }

        return div(label, input).attr("class", "form-input");
    }

}
