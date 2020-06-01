package pt.isel.ls.view.label;

import pt.isel.ls.handlers.label.GetLabelsHandler;
import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.text.AnchorText;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.detail.HtmlDetailBuilder;
import pt.isel.ls.view.utils.detail.StringDetailBuilder;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class LabelView extends View {

    private final Label label;
    private final Stream<Room> rooms;

    public LabelView(Label label, Stream<Room> rooms) {
        super("Label " + label.getName());
        this.label = label;
        this.rooms = rooms;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        List<AnchorText> roomsLst = rooms
                .map(r -> a(handler.route(GetRoomHandler.class, r.getRid()), r.getName()))
                .collect(Collectors.toList());

        Element el = new HtmlDetailBuilder()
                .withDetail("Id", label.getLid())
                .withDetail("Name", label.getName())
                .withDetail("Rooms", roomsLst)
                .build();

        return div(el);
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        List<String> roomsLst = rooms
                .map(Room::toString)
                .collect(Collectors.toList());

        writer.write(new StringDetailBuilder()
                .withDetail("Id", label.getLid())
                .withDetail("Name", label.getName())
                .withDetail("Rooms", roomsLst)
                .build());
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetLabelsHandler.class), "Labels");
    }

}
