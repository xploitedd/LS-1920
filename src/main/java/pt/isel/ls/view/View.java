package pt.isel.ls.view;

import java.io.PrintWriter;

public interface View {

    /**
     * Renders this view on the selected writer
     * @param writer writer where to render this view
     */
    void render(PrintWriter writer);

}
