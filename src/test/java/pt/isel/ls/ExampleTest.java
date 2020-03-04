package pt.isel.ls;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExampleTest {

    @Test
    public void example() {
        // arrange | given
        int a = 1;
        int b = 2;

        // act | when
        int result = a + b;

        // assert | then
        assertEquals(3, result);
    }

    @Test
    public void do_not_ignore_unexpected_exceptions_on_tests() throws FileNotFoundException {
        // test methods can have a non-empty `throws` exception list.
        assertThrows(FileNotFoundException.class, () -> {
            new FileInputStream("does-not-exist");
        });
    }
}
