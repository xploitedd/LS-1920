package pt.isel.ls.view.utils.form;

import pt.isel.ls.model.dsl.Dsl;

public class HtmlFormInput extends HtmlFormElement {

    public HtmlFormInput(String name, String description, InputType type, boolean required) {
        super(name, description, Dsl.input(type.getType(), name, required));
    }

}
