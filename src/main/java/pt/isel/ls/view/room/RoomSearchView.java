package pt.isel.ls.view.room;

import pt.isel.ls.handlers.room.GetRoomsHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.text.forms.OptionText;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.form.HtmlFormBuilder;
import pt.isel.ls.view.utils.form.HtmlFormInput;
import pt.isel.ls.view.utils.form.HtmlFormSelect;
import pt.isel.ls.view.utils.form.InputType;

import java.util.stream.StreamSupport;

import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.h1;
import static pt.isel.ls.model.dsl.Dsl.option;

public class RoomSearchView extends View {

    private final Iterable<Label> availableLabels;

    public RoomSearchView(Iterable<Label> availableLabels) {
        super("Search Room");
        this.availableLabels = availableLabels;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        Element el = new HtmlFormBuilder(Method.GET, handler.route(GetRoomsHandler.class))
                .withInput(new HtmlFormInput("begin", "Available From", InputType.DATETIME, false))
                .withInput(new HtmlFormInput("duration", "Available For", InputType.NUMBER, false)
                        .withAttr("min", "10")
                        .withAttr("step", "10"))
                .withInput(new HtmlFormInput("capacity", "With capacity", InputType.NUMBER, false)
                        .withAttr("min", "1")
                        .withAttr("step", "1"))
                .withInput(new HtmlFormSelect("label", "With labels", getOptions(), true))
                .build("Search");

        return div(
                h1("Search for a room"),
                el
        );
    }

    private OptionText[] getOptions() {
        return StreamSupport.stream(availableLabels.spliterator(), false)
                .map(label -> option(label.getName(), label.getName()))
                .toArray(OptionText[]::new);
    }

}
