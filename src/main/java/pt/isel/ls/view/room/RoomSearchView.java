package pt.isel.ls.view.room;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.form;
import static pt.isel.ls.model.dsl.Dsl.input;
import static pt.isel.ls.model.dsl.Dsl.label;


public class RoomSearchView extends View {

    public RoomSearchView(String title) {
        super(title);
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        return form(
                "get",
                handler.route("GetRoomsHandler"),
                div(
                        label("id1", "Begin"),
                        input("datetime-local","begin")
                                .attr("id","id1")
                ),
                div(
                        label("id2", "Duration"),
                        input("number", "duration")
                                .attr("id", "id2")
                                .attr("min", "10")
                                .attr("step", "10")
                ),
                div(
                        label("id3", "Capacity"),
                        input("number", "capacity")
                                .attr("id", "id3")
                                .attr("min", "0")
                ),
                div(label("id4", "Labels")));
    }
}
