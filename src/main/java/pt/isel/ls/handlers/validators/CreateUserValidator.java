package pt.isel.ls.handlers.validators;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Validator;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;

import java.util.regex.Pattern;

public class CreateUserValidator extends AbstractValidator {

    // obtained from https://owasp.org/www-community/OWASP_Validation_Regex_Repository
    private static final Pattern USER_EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    private static final int USER_NAME_MAX_LENGTH = 50;
    private static final int USER_EMAIL_MAX_LENGTH = 64;

    public CreateUserValidator(RouteRequest request, ConnectionProvider provider) {
        super(new Validator(request)
                .addMapping("name", p -> p.getUnique().toString())
                .addMapping("email", p -> p.getUnique().toString())
                .addFilter("name", name -> name.length() <= USER_NAME_MAX_LENGTH, String.class)
                .addFilter("email", email -> email.length() <= USER_EMAIL_MAX_LENGTH, String.class)
                .addFilter("email", email -> USER_EMAIL_PATTERN.matcher(email).matches(), String.class)
                .addFilter("email", email -> provider.execute(handler -> {
                    new UserQueries(handler).checkEmailAvailability(email);
                    return true;
                }), String.class));
    }

    public String getName() {
        return getParameter("name");
    }

    public String getEmail() {
        return getParameter("email");
    }

}
