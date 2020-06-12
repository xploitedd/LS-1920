package pt.isel.ls.handlers.validators;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Validator;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;

public class CreateLabelValidator extends AbstractValidator {

    private static final int LABEL_NAME_MAX_LENGTH = 50;

    public CreateLabelValidator(RouteRequest request, ConnectionProvider provider) {
        super(new Validator(request)
                .addMapping("name", p -> p.getUnique().toString())
                .addFilter("name", name -> name.length() <= LABEL_NAME_MAX_LENGTH,
                        String.class, "Name max length is " + LABEL_NAME_MAX_LENGTH)
                .addFilter("name", name -> provider.execute(handler -> {
                    new LabelQueries(handler).checkLabelAvailability(name);
                    return true;
                }), String.class));
    }

    public String getName() {
        return getParameter("name");
    }

}
