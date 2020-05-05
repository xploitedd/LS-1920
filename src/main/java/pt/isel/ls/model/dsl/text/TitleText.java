package pt.isel.ls.model.dsl.text;

public class TitleText extends TextNode {

    public TitleText(String text) {
        super(text);
    }

    @Override
    protected String getNodeName() {
        return "title";
    }

}
