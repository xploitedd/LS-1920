package pt.isel.ls.view.utils.form;

import pt.isel.ls.exceptions.parameter.ParameterNotFoundException;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.forms.FormElement;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Parameter;
import pt.isel.ls.router.response.error.ParameterErrors;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
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

    public HtmlFormBuilder withErrors(ParameterErrors errors) {
        if (errors != null) {
            HashSet<String> failedInputs = new HashSet<>();
            for (Map.Entry<String, String> err : errors.getErrors()) {
                String inputName = err.getKey();
                HtmlFormElement input = inputs.get(inputName);
                if (input != null) {
                    failedInputs.add(inputName);
                    input.withError(err.getValue());
                }
            }

            RouteRequest request = errors.getRequest();
            for (Map.Entry<String, HtmlFormElement> entry : inputs.entrySet()) {
                String input = entry.getKey();
                if (!failedInputs.contains(input)) {
                    HtmlFormElement el = entry.getValue();
                    try {
                        if (el instanceof HtmlFormSelect) {
                            List<String> options = request.getParameter(input)
                                    .map(Parameter::toString);

                            HtmlFormSelect sel = (HtmlFormSelect) el;
                            sel.selectOptions(options);
                        } else {
                            el.setValue(request.getParameter(input)
                                    .getUnique()
                                    .toString());
                        }
                    } catch (ParameterNotFoundException ignore) {
                        // unfortunately checkstyle doesn't love empty
                        // catch blocks :(, so here we give him some food:
                        continue;
                    }
                }
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
