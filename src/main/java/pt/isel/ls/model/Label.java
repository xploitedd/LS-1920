package pt.isel.ls.model;

public class Label {

    private final int lid;
    private final String name;

    public Label(int lid, String name) {
        this.lid = lid;
        this.name = name;
    }

    public int getLid() {
        return lid;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
