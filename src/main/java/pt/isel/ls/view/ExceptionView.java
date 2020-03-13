package pt.isel.ls.view;

public class ExceptionView implements View {

    private Exception exception;

    public ExceptionView(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void render() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("\tAn exception occurred: " + exception.getMessage());
        System.out.println("---------------------------------------------------------------------------");
    }

}
