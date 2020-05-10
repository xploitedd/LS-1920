package pt.isel.ls.utils;

import pt.isel.ls.exceptions.AppException;

import java.util.concurrent.Callable;

public class ExceptionUtils {

    public static <T> T passException(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

}
