package pt.isel.ls.view;

public class ExitView implements View {

    @Override
    public void render() {
        System.out.println("Closing Application. Bye!");
        try {
            Thread.sleep(2000);
            System.exit(0);
        } catch (InterruptedException e) {
            new ThrowableView(e).render();
        }
    }

}
