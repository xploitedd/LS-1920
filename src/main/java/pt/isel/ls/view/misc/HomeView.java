package pt.isel.ls.view.misc;

import pt.isel.ls.handlers.label.GetLabelsHandler;
import pt.isel.ls.handlers.misc.GetTimeHandler;
import pt.isel.ls.handlers.room.GetRoomSearchHandler;
import pt.isel.ls.handlers.user.GetUsersHandler;
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
        String time = handler.route(GetTimeHandler.class);
        String users = handler.route(GetUsersHandler.class);
        String search = handler.route(GetRoomSearchHandler.class);
        String labels = handler.route(GetLabelsHandler.class);

        return ul(
                li(a(time, "Time")),
                li(a(users, "Users")),
                li(a(search, "Search Rooms")),
                li(a(labels, "Labels"))
        );
    }
}
