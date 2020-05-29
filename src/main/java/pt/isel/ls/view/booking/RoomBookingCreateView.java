package pt.isel.ls.view.booking;

import pt.isel.ls.handlers.booking.PostRoomBookingCreateHandler;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.button;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.form;
import static pt.isel.ls.model.dsl.Dsl.input;
import static pt.isel.ls.model.dsl.Dsl.label;

public class RoomBookingCreateView extends View {

    private final Room room;

    public RoomBookingCreateView(Room room) {
        super("Create Booking");
        this.room = room;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        //TODO: Making the user input a numerical ID is bad,
        // make it email instead and convert it to id in PostRoomBookingCreateHandler
        // (Haven't done so yet because I need request.setParameter() to do this)
        return form(
                "post",
                handler.route(PostRoomBookingCreateHandler.class, room.getRid()),
                div(
                        label("id", "User identification number"),
                        input("number","uid")
                                .attr("id","id")
                ),
                div(
                        label("begin", "Begin"),
                        input("datetime-local","begin")
                                .attr("id","begin")
                ),
                div(
                        label("duration", "Duration"),
                        input("number", "duration")
                                .attr("id", "duration")
                                .attr("min", "10")
                                .attr("step", "10")
                ),
                div(
                        button("Create")
                                .attr("type", "submit")
                )
        );
    }
}
