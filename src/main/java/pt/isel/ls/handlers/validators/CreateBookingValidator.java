package pt.isel.ls.handlers.validators;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Validator;

public class CreateBookingValidator extends AbstractValidator {

    public CreateBookingValidator(RouteRequest request) {
        super(new Validator(request)
                .addMapping("uid", p -> p.getUnique().toInt())
                .addMapping("begin", p -> p.getUnique().toTime())
                .addMapping("duration", p -> p.getUnique().toInt()));
    }

    public int getUserId() {
        return getParameter("uid");
    }

    public long getBegin() {
        return getParameter("begin");
    }

    public int getDuration() {
        return getParameter("duration");
    }

}
