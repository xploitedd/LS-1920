package pt.isel.ls.utils;

@FunctionalInterface
public interface ThrowingRunnable {

    void run() throws Exception;

}
