package pt.isel.ls.view.utils.detail;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.text.TextNode;

import java.util.ArrayList;

import static pt.isel.ls.model.dsl.Dsl.b;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.text;

public class HtmlDetailBuilder extends DetailBuilder<TextNode, TextNode, Element> {

    public HtmlDetailBuilder withDetail(String key, Object value) {
        super.withDetail(b(key + ": "), text(value));
        return this;
    }

    @Override
    public Element build() {
        ArrayList<Element> detailList = new ArrayList<>(details.size());
        for (TextNode key : details.keySet()) {
            detailList.add(div(key, details.get(key)));
        }

        return div(detailList.toArray(Node[]::new));
    }

}
