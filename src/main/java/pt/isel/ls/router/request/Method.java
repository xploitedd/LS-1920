package pt.isel.ls.router.request;

import pt.isel.ls.exceptions.router.RouteException;

public enum Method {

    GET,
    POST,
    EXIT,
    OPTION,
    PUT,
    DELETE,
    LISTEN,
    CLOSE;

    /**
     * Get the method from a string
     * @param method string method
     * @return method
     */
    public static Method fromString(String method) {
        method = method.toUpperCase();
        try {
            return Method.valueOf(method);
        } catch (IllegalArgumentException e) {
            throw new RouteException("Method " + method + " does not exist");
        }
    }

}
