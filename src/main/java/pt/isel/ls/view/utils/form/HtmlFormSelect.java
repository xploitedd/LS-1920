package pt.isel.ls.view.utils.form;

import pt.isel.ls.model.dsl.elements.forms.SelectElement;
import pt.isel.ls.model.dsl.text.forms.OptionText;

import java.util.List;

import static pt.isel.ls.model.dsl.Dsl.select;

public class HtmlFormSelect extends HtmlFormElement {

    public HtmlFormSelect(String name, String description, OptionText[] options, boolean multiple) {
        super(name, description, select(name, multiple, options));
    }

    public HtmlFormSelect selectOptions(List<String> optionNames) {
        SelectElement sel = (SelectElement) input;
        OptionText[] options = sel.getOptions();
        for (OptionText opt : options) {
            if (optionNames.contains(opt.getAttr("value"))) {
                opt.attr("selected", "1");
            }
        }

        return this;
    }

}
