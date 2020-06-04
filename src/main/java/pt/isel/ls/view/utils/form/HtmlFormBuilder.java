package pt.isel.ls.view.utils.form;

import pt.isel.ls.model.dsl.Dsl;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.elements.forms.FormElement;
import pt.isel.ls.model.dsl.text.TextNode;
import pt.isel.ls.model.dsl.text.forms.OptionText;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.parameter.ParameterErrors;

import java.util.LinkedHashMap;

import static pt.isel.ls.model.dsl.Dsl.button;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.form;
import static pt.isel.ls.model.dsl.Dsl.input;
import static pt.isel.ls.model.dsl.Dsl.label;
import static pt.isel.ls.model.dsl.Dsl.select;

public class HtmlFormBuilder {

    private static final String DEFAULT_SUBMIT_TEXT = "Submit";

    private final LinkedHashMap<String, Element> contents = new LinkedHashMap<>();
    private final Method method;
    private final String action;
    private ParameterErrors errors;

    public HtmlFormBuilder(Method method, String action) {
        this.method = method;
        this.action = action;
    }

    public HtmlFormBuilder withInput(String name, String desc, InputType type) {
        return withInput(name, desc, type, false);
    }

    public HtmlFormBuilder withInput(String name, String desc, InputType type, boolean required) {
        contents.put(name, div(
                label(name, desc),
                input(type.getType(), name, required).attr("id", name)
        ).attr("class", "form-input"));

        return this;
    }

    public HtmlFormBuilder withNumber(String name, String desc, int min, int step) {
        return withNumber(name, desc, false, min, step);
    }

    public HtmlFormBuilder withNumber(String name, String desc, boolean required, int min, int step) {
        contents.put(name, div(
                label(name, desc),
                input(InputType.NUMBER.getType(), name, required)
                        .attr("id", name)
                        .attr("min", String.valueOf(min))
                        .attr("step", String.valueOf(step))
        ).attr("class", "form-input"));

        return this;
    }

    public HtmlFormBuilder withOptions(String name, String desc, OptionText[] options, boolean multiple) {
        contents.put(name, div(
                label(name, desc),
                select(name, multiple, options)
                        .attr("id", name)
        ).attr("class", "form-input"));

        return this;
    }

    public HtmlFormBuilder withErrors(ParameterErrors errors) {
        this.errors = errors;
        return this;
    }

    public Element build() {
        return build(DEFAULT_SUBMIT_TEXT);
    }

    public Element build(String submitText) {
        contents.put("__submit_button_id", div(
            button(submitText).attr("type", "submit")
        ));

        Element[] elements = contents.values().toArray(Element[]::new);
        FormElement form = form(method.name(), action, elements);

        if (errors != null) {
            TextNode[] err = errors.getErrors()
                    .values()
                    .stream()
                    .map(Dsl::p)
                    .toArray(TextNode[]::new);

            return div(
                    div(err),
                    form
            );
        }

        return div(form);
    }


}
