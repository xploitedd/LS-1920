package pt.isel.ls.view.room;

import pt.isel.ls.handlers.room.GetRoomsHandler;
import pt.isel.ls.handlers.room.PostRoomCreateHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.dsl.elements.forms.FormElement;
import pt.isel.ls.model.dsl.text.forms.OptionText;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.response.error.HandlerErrors;
import pt.isel.ls.router.response.error.ParameterErrors;
import pt.isel.ls.view.FormView;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.form.HtmlFormBuilder;
import pt.isel.ls.view.utils.form.HtmlFormInput;
import pt.isel.ls.view.utils.form.HtmlFormSelect;
import pt.isel.ls.view.utils.form.InputType;

import java.util.stream.StreamSupport;

import static pt.isel.ls.model.dsl.Dsl.option;

public class RoomCreateView extends FormView {

    private final Iterable<Label> availableLabels;

    public RoomCreateView(Iterable<Label> availableLabels) {
        super("Create a new room");
        this.availableLabels = availableLabels;
    }

    public RoomCreateView(Iterable<Label> availableLabels, ParameterErrors errors) {
        super("Create a new room", errors);
        this.availableLabels = availableLabels;
    }

    public RoomCreateView(Iterable<Label> availableLabels, HandlerErrors errors) {
        super("Create a new room", errors);
        this.availableLabels = availableLabels;
    }

    @Override
    protected FormElement getForm(ViewHandler handler) {
        return new HtmlFormBuilder(Method.POST, handler.route(PostRoomCreateHandler.class))
                .withInput(new HtmlFormInput("name", "Room Name", InputType.TEXT, true))
                .withInput(new HtmlFormInput("location", "Location", InputType.TEXT, true))
                .withInput(new HtmlFormInput("capacity", "Capacity", InputType.NUMBER, true)
                        .withAttr("min", "2")
                        .withAttr("step", "1"))
                .withInput(new HtmlFormInput("description", "Description", InputType.TEXT, false))
                .withInput(new HtmlFormSelect("label", "Labels", getOptions(), true))
                .withErrors(parameterErrors)
                .build("Create Room");
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
