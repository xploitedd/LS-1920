package pt.isel.ls.view.misc;

import pt.isel.ls.router.Router;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.table.StringTableBuilder;

import java.io.PrintWriter;

public class OptionView extends View {

    private final Iterable<Router.Route> options;

    public OptionView(Iterable<Router.Route> options) {
        super("Options");
        this.options = options;
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        writer.write(new StringTableBuilder<>(options)
                .withColumn("Method", Router.Route::getMethod)
                .withColumn("Template", Router.Route::getRouteTemplate)
                .withColumn("Description", r -> r.getHandler().getDescription())
                .build());
    }
}
