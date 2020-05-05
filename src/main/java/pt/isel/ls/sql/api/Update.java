package pt.isel.ls.sql.api;

import java.sql.PreparedStatement;

public class Update extends SqlType<Update, Integer> {

    private int stmtIdx;

    Update(PreparedStatement stmt) {
        super(stmt);
    }

    @Override
    public Integer execute() {
        return SqlHandler.passException(stmt::executeUpdate);
    }

}
