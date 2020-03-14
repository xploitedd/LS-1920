package pt.isel.ls.view.console;

public class MessageView implements View {

    private String message;

    public MessageView(String message) {
        this.message = message;
    }

    @Override
    public void render() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("\t" + message);
        System.out.println("---------------------------------------------------------------------------");
    }
}
