package pt.isel.ls.dsl.text.table;

public class TableDataText extends TableText {

    public TableDataText(String text) {
        super(text);
    }

    @Override
    protected String getNodeName() {
        return "td";
    }

}
