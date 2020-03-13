package pt.isel.ls.router;

import pt.isel.ls.view.View;

public class RouteResponse {

    private final View view;
    private String contentType;
    private int statusCode;

    public RouteResponse(View view) {
        this.view = view;
        this.contentType = "application/text";
        this.statusCode = 200;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public RouteResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public RouteResponse setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public View getView() {
        return view;
    }

}
