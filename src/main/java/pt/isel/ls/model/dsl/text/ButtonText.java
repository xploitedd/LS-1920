package pt.isel.ls.model.dsl.text;

public class ButtonText extends TextNode {

    public ButtonText(String text) {
        super(text);
    }

    @Override
    protected String getNodeName() {
        return "button";
    }

}
