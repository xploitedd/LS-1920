package pt.isel.ls.app;

import pt.isel.ls.router.Router;

public abstract class Application implements Runnable {

    protected final Router router;

    public Application(Router router) {
        this.router = router;
    }

}
