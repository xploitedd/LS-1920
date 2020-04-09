package pt.isel.ls.dsl.text.table;

public class TableHeaderText extends TableText {

    public TableHeaderText(String text) {
        super(text);
    }

    @Override
    protected String getNodeName() {
        return "th";
    }

}
