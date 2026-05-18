import configuration.ApplicationConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;


//

public class Main {

    private static final String BASE_URI =
            "http://localhost:8080/";

    public static void main(String[] args) throws IOException {

        HttpServer server =
                GrizzlyHttpServerFactory.createHttpServer(
                        URI.create(BASE_URI),
                        new ApplicationConfig()
                );
        System.out.println("Server started:");
        System.out.println(BASE_URI);
        System.in.read();
        server.shutdownNow();
    }



}