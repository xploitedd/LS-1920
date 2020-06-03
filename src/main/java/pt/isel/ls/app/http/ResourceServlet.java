package pt.isel.ls.app.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResourceServlet extends HttpServlet {

    private static final String RESOURCE_FOLDER = "public/";
    private static final Logger log = LoggerFactory.getLogger(ResourceServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI().substring(1);
        Charset charset = StandardCharsets.UTF_8;
        resp.setCharacterEncoding(charset.name());

        OutputStream out = resp.getOutputStream();
        try (BufferedReader br = new BufferedReader(new FileReader(RESOURCE_FOLDER + uri))) {
            int buf;
            while ((buf = br.read()) != -1) {
                out.write(buf);
            }
        } catch (FileNotFoundException e) {
            resp.sendError(404, "Requested file not found!");
            return;
        }

        out.flush();
    }

}
