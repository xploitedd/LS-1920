package pt.isel.ls.view.booking;

import pt.isel.ls.handlers.booking.GetRoomBookingsHandler;
import pt.isel.ls.handlers.booking.PostRoomBookingCreateHandler;
import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.dsl.elements.forms.FormElement;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.error.HandlerError;
import pt.isel.ls.router.response.error.ParameterError;
import pt.isel.ls.view.FormView;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.form.HtmlFormBuilder;
import pt.isel.ls.view.utils.form.HtmlFormInput;
import pt.isel.ls.view.utils.form.InputType;

public class RoomBookingCreateView extends FormView {

    private final Room room;

    public RoomBookingCreateView(Room room) {
        super(String.format("Create booking in \"%s\" room", room.getName()));
        this.room = room;
    }

    public RoomBookingCreateView(Room room, ParameterError error, RouteRequest request) {
        super(String.format("Create booking in \"%s\" room", room.getName()), error, request);
        this.room = room;
    }

    public RoomBookingCreateView(Room room, HandlerError error) {
        super(String.format("Create booking in \"%s\" room", room.getName()), error);
        this.room = room;
    }

    @Override
    protected FormElement getForm(ViewHandler handler) {
        return new HtmlFormBuilder(
                Method.POST,
                handler.route(PostRoomBookingCreateHandler.class, room.getRid()))
                .withInput(new HtmlFormInput("email", "User Email", InputType.EMAIL, true))
                .withInput(new HtmlFormInput("begin", "Begin", InputType.DATETIME, true))
                .withInput(new HtmlFormInput("duration", "Duration", InputType.NUMBER, true)
                        .withAttr("min", "10")
                        .withAttr("step", "10"))
                .withErrors(parameterErrors, request)
                .build("Create Booking");
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetRoomHandler.class, room.getRid()), "Room");
        addNavEntry(handler.route(GetRoomBookingsHandler.class, room.getRid()), "Room Bookings");
    }

}
