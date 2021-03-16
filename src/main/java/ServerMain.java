import spark.*;
import org.apache.commons.io.FileUtils;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

public class ServerMain {

    public static void main(String[] args) {
        enableDebugScreen();

        File uploadDir = new File("upload");
        uploadDir.mkdir(); // create the upload directory if it doesn't exist
        
        staticFiles.externalLocation("upload");
        
        //options and before are here to handle CORS problems; CAUTION : this is no safe but OK for an academic project
        
        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        
        get("/result", (req, res) -> {
        	
        	System.out.println("Asked for result");
        	String string_json = Files.readString(Paths.get("./results/log_results.json"));
        	return string_json;
        });
        
        get("/clean", (req, res) -> {
        	
        	System.out.println("Directories results and upload have been emptied");
        	FileUtils.cleanDirectory(new File("./results"));
        	FileUtils.cleanDirectory(new File("./upload"));
        	return "Clean";
        });

        post("/upload", (req, res) -> {

            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", ".json");

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            logInfo(req, tempFile);
            String input_path = tempFile.toString();
            //return "<h1>You uploaded this image:<h1><img src='" + tempFile.getFileName() + "'>";
            String[] arg_list = new String[1];
            arg_list[0] = input_path;
            MMLMain.main(arg_list);
            String string_json = Files.readString(Paths.get("./results/log_results.json")); // to do: adapt the filename
            System.out.println(string_json);
            //res.redirect("index_projetv3.html");
            return string_json;

        });

    }

    // methods used for logging
    private static void logInfo(Request req, Path tempFile) throws IOException, ServletException {
        System.out.println("Uploaded file '" + getFileName(req.raw().getPart("uploaded_file")) + "' saved as '" + tempFile.toAbsolutePath() + "'");
    }

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}