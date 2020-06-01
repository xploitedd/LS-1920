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
                .withInput("begin", "Available From", InputType.DATETIME)
                .withNumber("duration", "Available For", 10, 10)
                .withNumber("capacity", "With capacity", 0, 1)
                .withOptions("label", "With labels", getOptions(), true)
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
