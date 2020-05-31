package pt.isel.ls.app.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResourceServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ResourceServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI().substring(1);
        InputStream is = ClassLoader.getSystemResourceAsStream(uri);
        if (is == null) {
            log.error("Error opening resource at {}", uri);
            resp.setStatus(500);
            return;
        }

        Charset charset = StandardCharsets.UTF_8;
        resp.setCharacterEncoding(charset.name());

        OutputStream out = resp.getOutputStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            int buf;
            while ((buf = br.read()) != -1) {
                out.write(buf);
            }
        }

        out.flush();
    }

}
