package pt.isel.ls.view.utils.form;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.forms.FormElement;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.response.error.ParameterError;

import java.util.LinkedHashMap;
import java.util.Map;

import static pt.isel.ls.model.dsl.Dsl.button;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.form;

public class HtmlFormBuilder {

    private static final String DEFAULT_SUBMIT_TEXT = "Submit";

    private final LinkedHashMap<String, HtmlFormElement> inputs = new LinkedHashMap<>();
    private final Method method;
    private final String action;

    public HtmlFormBuilder(Method method, String action) {
        this.method = method;
        this.action = action;
    }

    public HtmlFormBuilder withInput(HtmlFormElement input) {
        inputs.put(input.getName(), input);
        return this;
    }

    public HtmlFormBuilder withErrors(ParameterError errors) {
        if (errors == null) {
            return this;
        }

        for (Map.Entry<String, String> err : errors.getErrors()) {
            String inputName = err.getKey();
            HtmlFormElement input = inputs.get(inputName);
            if (input != null) {
                input.withError(err.getValue());
            }
        }

        return this;
    }

    public FormElement build() {
        return build(DEFAULT_SUBMIT_TEXT);
    }

    public FormElement build(String submitText) {
        Node[] nodes = inputs.values()
                .stream()
                .map(HtmlFormElement::getContainer)
                .toArray(Node[]::new);


        return form(method.name(), action, div(
                div(nodes),
                button(submitText).attr("type", "submit")
        ));
    }


}
