package pt.isel.ls.view.room;

import pt.isel.ls.handlers.room.PostRoomCreateHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.forms.SelectElement;
import pt.isel.ls.model.dsl.text.forms.OptionText;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import java.util.stream.StreamSupport;

import static pt.isel.ls.model.dsl.Dsl.button;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.form;
import static pt.isel.ls.model.dsl.Dsl.input;
import static pt.isel.ls.model.dsl.Dsl.label;
import static pt.isel.ls.model.dsl.Dsl.option;
import static pt.isel.ls.model.dsl.Dsl.select;

public class RoomCreateView extends View {

    private final Iterable<Label> availableLabels;

    public RoomCreateView(Iterable<Label> availableLabels) {
        super("Create Room");
        this.availableLabels = availableLabels;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        return form(
                "post",
                handler.route(PostRoomCreateHandler.class),
                div(
                        label("rname", "Room Name"),
                        input("text","name")
                                .attr("id","rname")
                ),
                div(
                        label("local", "Location"),
                        input("text", "location")
                                .attr("id", "local")
                ),
                div(
                        label("capacity", "Capacity"),
                        input("number", "capacity")
                                .attr("id", "capacity")
                                .attr("min", "0")
                ),
                div(
                        label("labels", "Labels"),
                        getLabelSelector()
                                .attr("id", "labels")
                ),
                div(
                        button("Search")
                                .attr("type", "submit")
                )
        );
    }

    private SelectElement getLabelSelector() {
        OptionText[] options = StreamSupport.stream(availableLabels.spliterator(), false)
                .map(label -> option(label.getName(), label.getName()))
                .toArray(OptionText[]::new);

        return select("label", true, options);
    }
}
