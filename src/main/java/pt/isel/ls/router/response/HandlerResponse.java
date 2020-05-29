package pt.isel.ls.router.response;

import java.util.Objects;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.view.View;
import pt.isel.ls.view.misc.EmptyView;

public class HandlerResponse {

    private final View view;
    private Redirect redirect;
    private StatusCode statusCode;

    /**
     * Creates a new RouteResponse with EmptyView
     */
    public HandlerResponse() {
        this(new EmptyView());
    }

    /**
     * Creates a new RouteResponse with a view
     * @param view view associated with this response
     */
    public HandlerResponse(View view) {
        this.view = view;
        this.statusCode = StatusCode.OK;
    }

    /**
     * Gets this Response status code
     * Default: 200
     * @return status code
     */
    public StatusCode getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the status code for this Response
     * @param statusCode status code to be set
     * @return the modified instance of RouteResponse
     */
    public HandlerResponse setStatusCode(StatusCode statusCode) {
        if (redirect == null) {
            // only can change the status code if this is not a redirect
            this.statusCode = statusCode;
        }

        return this;
    }

    public HandlerResponse redirect(Class<? extends RouteHandler> handler, Object... params) {
        if (redirect != null) {
            // can't change the response redirect later
            return this;
        }

        setStatusCode(StatusCode.SEE_OTHER);
        this.redirect = new Redirect(this, handler, params);
        return this;
    }

    public Redirect getRedirect() {
        return redirect;
    }

    public boolean hasRedirect() {
        return redirect != null;
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
