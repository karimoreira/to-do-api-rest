import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoApi {

    static List<String> tarefas = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/tarefas", new HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                String method = exchange.getRequestMethod();

                if (method.equalsIgnoreCase("GET")) {
                    String response = String.join("\n", tarefas);
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else if (method.equalsIgnoreCase("POST")) {
                    InputStream is = exchange.getRequestBody();
                    Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
                    String body = scanner.useDelimiter("\\A").next();
                    tarefas.add(body.trim());
                    String response = "Tarefa adicionada.";
                    exchange.sendResponseHeaders(201, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    String response = "Método não suportado";
                    exchange.sendResponseHeaders(405, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
    }
}
