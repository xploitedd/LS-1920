package pt.isel.ls.view.misc;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.router.Router;
import pt.isel.ls.view.View;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.li;
import static pt.isel.ls.model.dsl.Dsl.ul;

public class HomeView extends View {

    public HomeView() {
        super("Home", false);
    }

    @Override
    protected Node getHtmlBody(Router router) {
        String time = router.routeFromName("GetTimeHandler");
        String users = router.routeFromName("GetUsersHandler");
        // TODO: search route
        String labels = router.routeFromName("GetLabelsHandler");

        return ul(
                li(a(time, "Time")),
                li(a(users, "Users")),
                li(a(labels, "Labels"))
        );
    }
}
