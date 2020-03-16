package pt.isel.ls.router;

import pt.isel.ls.view.console.View;

public class RouteResponse {

    private final View view;
    private String contentType;
    private int statusCode;

    /**
     * Creates a new RouteResponse with a view
     * @param view view associated with this response
     */
    public RouteResponse(View view) {
        this.view = view;
        this.contentType = "application/text";
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
     * Gets this Response content type
     * Default: application/text
     * @return content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the status code for this Response
     * @param statusCode status code to be set
     * @return the modified instance of RouteResponse
     */
    public RouteResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * Sets the content type for this response
     * @param contentType content type to be set
     * @return the modified instance of RouteResponse
     */
    public RouteResponse setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Gets the current view associated with this Response
     * @return RouteResponse current view
     */
    public View getView() {
        return view;
    }

}
