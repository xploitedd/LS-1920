package pt.isel.ls.view.room;

import pt.isel.ls.handlers.room.GetRoomCreateHandler;
import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.handlers.room.GetRoomSearchHandler;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.table.HtmlTableBuilder;
import pt.isel.ls.view.utils.table.StringTableBuilder;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class RoomsView extends View {

    private final Iterable<Room> rooms;

    public RoomsView(Iterable<Room> rooms) {
        super("Rooms");
        this.rooms = rooms;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        Element el = new HtmlTableBuilder<>(rooms)
                .withColumn("Id", Room::getRid)
                .withColumn("Name", Room::getName)
                .withColumn("Capacity", Room::getCapacity)
                .withColumn("Location", Room::getLocation)
                .withColumn("Description", Room::getDescription)
                .withColumn("Room Link", r -> a(
                        handler.route(GetRoomHandler.class, r.getRid()),
                        "Show details"
                ))
                .build();

        return div(el);
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        writer.write(new StringTableBuilder<>(rooms)
                .withColumn("Id", Room::getRid)
                .withColumn("Name", Room::getName)
                .withColumn("Capacity", Room::getCapacity)
                .withColumn("Location", Room::getLocation)
                .withColumn("Description", Room::getDescription)
                .build());
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetRoomCreateHandler.class), "Create Room");
        addNavEntry(handler.route(GetRoomSearchHandler.class), "Search Rooms");
    }

}
