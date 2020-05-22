package pt.isel.ls.router.response;

import java.util.Objects;
import pt.isel.ls.view.View;

public class HandlerResponse {

    private final View view;
    private int statusCode;

    /**
     * Creates a new RouteResponse with a view
     * @param view view associated with this response
     */
    public HandlerResponse(View view) {
        this.view = view;
        this.statusCode = 200;
    }

    /**
     * Gets this Response status code
     * Default: 200
     * @return status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the status code for this Response
     * @param statusCode status code to be set
     * @return the modified instance of RouteResponse
     */
    public HandlerResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * Gets the current view associated with this Response
     * @return RouteResponse current view
     */
    public View getView() {
        return view;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HandlerResponse that = (HandlerResponse) o;
        return statusCode == that.statusCode
                && view.equals(that.view);
    }

    @Override
    public int hashCode() {
        return Objects.hash(view, statusCode);
    }

}
