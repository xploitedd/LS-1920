package pt.isel.ls.view.booking;

import pt.isel.ls.handlers.booking.GetRoomBookingsHandler;
import pt.isel.ls.handlers.booking.PostRoomBookingCreateHandler;
import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.parameter.ParameterErrors;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.form.HtmlFormBuilder;
import pt.isel.ls.view.utils.form.InputType;

import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.h1;
import static pt.isel.ls.model.dsl.Dsl.h3;

public class RoomBookingCreateView extends View {

    private final Room room;
    private ParameterErrors errors;
    private String globalError;

    public RoomBookingCreateView(Room room, String globalError) {
        super("Create Booking");
        this.room = room;
        this.globalError = globalError;
    }

    public RoomBookingCreateView(Room room, ParameterErrors errors) {
        super("Create Booking");
        this.room = room;
        this.errors = errors;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        Element el = new HtmlFormBuilder(
                Method.POST,
                handler.route(PostRoomBookingCreateHandler.class, room.getRid()))
                .withInput("email", "User Email", InputType.EMAIL, true)
                .withInput("begin", "Begin", InputType.DATETIME, true)
                .withNumber("duration", "Duration", true, 10, 10)
                .withErrors(errors)
                .build("Create Booking");

        if (globalError == null) {
            return div(
                    h1(String.format("Create booking in \"%s\" room", room.getName())),
                    el
            );
        }

        return div(
                h1(String.format("Create booking in \"%s\" room", room.getName())),
                h3(globalError),
                el
        );
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetRoomHandler.class, room.getRid()), "Room");
        addNavEntry(handler.route(GetRoomBookingsHandler.class, room.getRid()), "Room Bookings");
    }

}
