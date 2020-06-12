package pt.isel.ls.handlers.validators;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Parameter;
import pt.isel.ls.router.request.validator.Validator;

import java.util.List;
import java.util.Optional;

public class GetRoomsValidator extends AbstractValidator {

    public GetRoomsValidator(RouteRequest request) {
        super(new Validator(request)
                .addMapping("begin", p -> p.getUnique().toTime(), true)
                .addMapping("duration", p -> p.getUnique().toInt(), true)
                .addMapping("capacity", p -> p.getUnique().toInt(), true)
                .addMapping("label", p -> p.map(Parameter::toString), true));
    }

    public Optional<Long> getBegin() {
        return getOptionalParameter("begin");
    }

    public Optional<Integer> getDuration() {
        return getOptionalParameter("duration");
    }

    public Optional<Integer> getCapacity() {
        return getOptionalParameter("capacity");
    }

    public Optional<List<String>> getLabels() {
        return getOptionalParameter("label");
    }

}
