package pt.isel.ls.view.room;

import pt.isel.ls.handlers.room.GetRoomsHandler;
import pt.isel.ls.handlers.room.PostRoomCreateHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.text.forms.OptionText;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.parameter.ParameterErrors;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.form.HtmlFormBuilder;
import pt.isel.ls.view.utils.form.InputType;

import java.util.stream.StreamSupport;

import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.h1;
import static pt.isel.ls.model.dsl.Dsl.option;

public class RoomCreateView extends View {

    private final Iterable<Label> availableLabels;
    private final ParameterErrors errors;

    public RoomCreateView(Iterable<Label> availableLabels) {
        this(availableLabels, null);
    }

    public RoomCreateView(Iterable<Label> availableLabels, ParameterErrors errors) {
        super("Create Room");
        this.availableLabels = availableLabels;
        this.errors = errors;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        Element el = new HtmlFormBuilder(Method.POST, handler.route(PostRoomCreateHandler.class))
                .withInput("name", "Room Name", InputType.TEXT, true)
                .withInput("location", "Location", InputType.TEXT, true)
                .withNumber("capacity", "Capacity", true, 1, 1)
                .withInput("description", "Description", InputType.TEXT)
                .withOptions("label", "Labels", getOptions(), true)
                .withErrors(errors)
                .build("Create Room");

        return div(
                h1("Create a new room"),
                el
        );
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetRoomsHandler.class), "Rooms");
    }

    private OptionText[] getOptions() {
        return StreamSupport.stream(availableLabels.spliterator(), false)
                .map(label -> option(label.getName(), label.getName()))
                .toArray(OptionText[]::new);
    }
}
