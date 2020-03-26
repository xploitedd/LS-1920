package pt.isel.ls.router;

import java.util.Optional;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class PathTest {

    @Test
    public void testCreatePathEmpty() {
        assertEquals(Optional.empty(), Path.of(null));
        assertEquals(Optional.empty(), Path.of(""));
        assertEquals(Optional.empty(), Path.of("              "));
    }

    @Test
    public void testCreatePathRoot() {
        Optional<Path> root = Path.of("/");
        assertNotSame(Optional.empty(), root);
        assertEquals(0, root.get().getPathSegments().length);
        assertNotSame(Optional.empty(), Path.of("/         "));
        assertNotSame(Optional.empty(), Path.of("      /         "));
    }

    @Test
    public void testCreateInvalidPath() {
        assertEquals(Optional.empty(), Path.of("test/"));
    }

    @Test
    public void testCreateValidPath() {
        Optional<Path> path = Path.of("/test");
        assertNotSame(Optional.empty(), path);
        assertEquals("/test", path.get().toString());
    }

}
