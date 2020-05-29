package pt.isel.ls.router;

public enum StatusCode {

    OK(200),
    CREATED(201),
    ACCEPTED(202),
    SEE_OTHER(303),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SEVER_ERROR(500);

    private final int statusCode;

    StatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getCode() {
        return statusCode;
    }

}
