package pt.isel.ls.model.dsl.text;

public class BoldText extends TextNode {

    public BoldText(Object text) {
        super(text);
    }

    @Override
    protected String getNodeName() {
        return "b";
    }

}
