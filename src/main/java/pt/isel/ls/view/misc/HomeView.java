package pt.isel.ls.view.misc;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.li;
import static pt.isel.ls.model.dsl.Dsl.ul;

public class HomeView extends View {

    public HomeView() {
        super("Home", false);
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        String time = handler.route("GetTimeHandler");
        String users = handler.route("GetUsersHandler");
        // TODO: search route
        String labels = handler.route("GetLabelsHandler");

        return ul(
                li(a(time, "Time")),
                li(a(users, "Users")),
                li(a(labels, "Labels"))
        );
    }
}
