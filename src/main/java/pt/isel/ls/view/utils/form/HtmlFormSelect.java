package pt.isel.ls.view.utils.form;

import pt.isel.ls.model.dsl.text.forms.OptionText;

import static pt.isel.ls.model.dsl.Dsl.select;

public class HtmlFormSelect extends HtmlFormElement {

    public HtmlFormSelect(String name, String description, OptionText[] options, boolean multiple) {
        super(name, description, select(name, multiple, options));
    }

}
