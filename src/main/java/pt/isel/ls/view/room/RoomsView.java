package pt.isel.ls.view.room;

import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.handlers.room.GetRoomSearchHandler;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.table.TableElement;
import pt.isel.ls.utils.HtmlTableBuilder;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.td;
import static pt.isel.ls.model.dsl.Dsl.th;

public class RoomsView extends View {

    private final Iterable<Room> rooms;

    public RoomsView(String tableName, Iterable<Room> rooms) {
        super(tableName);
        this.rooms = rooms;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        TableElement tableEl = new HtmlTableBuilder<>(rooms)
                .withColumn(th("Id"), r -> td(r.getRid()))
                .withColumn(th("Name"), r -> td(r.getName()))
                .withColumn(th("Capacity"), r -> td(r.getCapacity()))
                .withColumn(th("Location"), r -> td(r.getLocation()))
                .withColumn(th("Description"), r -> td(r.getDescription()))
                .withColumn(th("Room Link"), r -> td(a(
                        handler.route(GetRoomHandler.class, r.getRid()),
                        "Show details"
                )))
                .build();

        return div(
                tableEl,
                a(handler.route(GetRoomSearchHandler.class), "Search Rooms")
        );
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        super.renderText(handler, writer);
    }

}
