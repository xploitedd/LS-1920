package pt.isel.ls.handlers.validators;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Validator;

import java.util.Optional;

public class ListenValidator extends AbstractValidator {

    public ListenValidator(RouteRequest request) {
        super(new Validator(request)
                .addMapping("port", p -> p.getUnique().toInt(), true));
    }

    public Optional<Integer> getPort() {
        return getOptionalParameter("port");
    }

}
