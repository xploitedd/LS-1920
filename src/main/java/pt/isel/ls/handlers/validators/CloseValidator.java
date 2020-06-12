package pt.isel.ls.handlers.validators;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Validator;

public class CloseValidator extends AbstractValidator {

    public CloseValidator(RouteRequest request) {
        super(new Validator(request)
                .addMapping("port", p -> p.getUnique().toInt()));
    }

    public int getPort() {
        return getParameter("port");
    }

}
