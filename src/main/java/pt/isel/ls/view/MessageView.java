package pt.isel.ls.view;

public class MessageView implements View {

    private String message;

    public MessageView(String msg){ message = msg; }

    @Override
    public void render() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("\t" + message);
        System.out.println("---------------------------------------------------------------------------");
    }
}
