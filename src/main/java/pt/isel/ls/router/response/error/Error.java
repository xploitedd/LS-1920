package pt.isel.ls.router.response.error;

import pt.isel.ls.model.dsl.Dsl;
import pt.isel.ls.model.dsl.Node;

import java.util.LinkedList;
import java.util.List;

import static pt.isel.ls.model.dsl.Dsl.div;

public class Error<T> {

    protected final LinkedList<T> errors = new LinkedList<>();

    public void addError(T error) {
        errors.add(error);
    }

    public List<T> getErrors() {
        return errors;
    }

    public Node toHtml() {
        Node[] err = errors.stream()
                .map(Dsl::h3)
                .toArray(Node[]::new);

        return div(err);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T error : errors) {
            sb.append(error.toString()).append('\n');
        }

        return sb.toString();
    }

}
