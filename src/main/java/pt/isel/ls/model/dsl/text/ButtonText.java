package pt.isel.ls.model.dsl.text;

public class ButtonText extends TextNode {

    public ButtonText(Object text) {
        super(text);
    }

    @Override
    protected String getNodeName() {
        return "button";
    }

}
