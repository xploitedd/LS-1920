package pt.isel.ls.model;

public final class Label {

    private final int lid;
    private final String name;

    /**
     * Creates a new Label
     * @param lid id of the label
     * @param name name of the label
     */
    public Label(int lid, String name) {
        this.lid = lid;
        this.name = name;
    }

    /**
     * Get Label Id
     * @return Label Id
     */
    public int getLid() {
        return lid;
    }

    /**
     * Get Label Name
     * @return Label Name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
