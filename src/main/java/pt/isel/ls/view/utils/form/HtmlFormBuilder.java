package pt.isel.ls.view.utils.form;

import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.elements.forms.FormElement;
import pt.isel.ls.router.request.Method;

import java.util.HashMap;

import static pt.isel.ls.model.dsl.Dsl.button;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.form;
import static pt.isel.ls.model.dsl.Dsl.input;
import static pt.isel.ls.model.dsl.Dsl.label;
import static pt.isel.ls.model.dsl.Dsl.p;

public class HtmlFormBuilder {

    private static final String DEFAULT_SUBMIT_TEXT = "Submit";

    private final HashMap<String, Element> contents = new HashMap<>();
    private final Method method;
    private final String action;
    private String error;

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
        ));

        return this;
    }

    public HtmlFormBuilder withError(String error) {
        this.error = error;
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

        if (error != null) {
            return div(
                    p(error),
                    form
            );
        }

        return div(form);
    }


}
