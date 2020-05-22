package pt.isel.ls.handlers.label;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.label.LabelsView;

import java.util.stream.Collectors;

public final class GetLabelsHandler extends RouteHandler {

    public GetLabelsHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/labels",
                "Gets all existing Labels",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        Iterable<Label> labels = provider.execute(handler -> new LabelQueries(handler)
                .getLabels()
                .collect(Collectors.toList()));

        return new HandlerResponse(new LabelsView(labels));
    }

}
