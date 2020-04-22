package pt.isel.ls.utils;

import java.util.concurrent.Callable;

public class ExceptionUtils {

    public static <T> T propagate(Callable<T> supplier) {
        try {
            return supplier.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
