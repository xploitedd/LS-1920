package pt.isel.ls.sql.api;

import java.sql.PreparedStatement;

import static pt.isel.ls.utils.ExceptionUtils.passException;

public class Update extends SqlType<Update, Integer> {

    Update(PreparedStatement stmt) {
        super(stmt);
    }

    @Override
    public Integer execute() {
        return passException(stmt::executeUpdate);
    }

}
