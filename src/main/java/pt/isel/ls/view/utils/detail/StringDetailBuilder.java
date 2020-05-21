package pt.isel.ls.view.utils.detail;

public class StringDetailBuilder extends DetailBuilder<String, Object, String> {

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        for (String key : details.keySet()) {
            sb.append(key)
                    .append(": ")
                    .append(details.get(key))
                    .append("\n");
        }

        return sb.toString();
    }

}
