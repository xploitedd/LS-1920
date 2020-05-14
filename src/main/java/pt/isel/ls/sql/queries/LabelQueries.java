package pt.isel.ls.sql.queries;

import java.util.Optional;
import java.util.stream.Stream;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.model.Label;
import pt.isel.ls.sql.api.SqlHandler;

public class LabelQueries extends DatabaseQueries {

    public LabelQueries(SqlHandler handler) {
        super(handler);
    }

    /**
     * Create a new Label
     * @param labelName name of the label to be created
     * @return the created label
     */
    public Label createNewLabel(String labelName) {
        handler.createUpdate("INSERT INTO label (name) VALUES (?);")
                .bind(labelName)
                .execute();

        return getLabel(labelName);
    }

    /**
     * Get a label by name
     * @param name name of the label
     * @return associated label
     */
    public Label getLabel(String name) {
        Optional<Label> label = handler.createQuery("SELECT * FROM label WHERE name = ?;")
                .bind(name)
                .mapToClass(Label.class)
                .findFirst();

        if (label.isEmpty()) {
            throw new RouteException("Label '" + name + "' not found");
        }

        return label.get();
    }

    /**
     * Get a label by id
     * @param lid id of the label
     * @return associated label
     */
    public Label getLabelById(int lid) {
        Optional<Label> label = handler.createQuery("SELECT * FROM label WHERE lid = ?;")
                .bind(lid)
                .mapToClass(Label.class)
                .findFirst();

        if (label.isEmpty()) {
            throw new RouteException("Label '" + lid + "' not found");
        }

        return label.get();
    }

    /**
     * Get all labels
     * @return every label available
     */
    public Stream<Label> getLabels() {
        return handler.createQuery("SELECT * FROM label")
                .mapToClass(Label.class);
    }

}
