package com.middleware;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import com.middleware.communication.Message;
import com.middleware.communication.Response;
import com.middleware.communication.MessageType;

@Slf4j
public class ServerRequestHandler {
    private int port = 8001;

    public ServerRequestHandler(){}
    
    public ServerRequestHandler(int port){
        this.port = port;
    }

    public void run() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);
        try {
            log.info("Server Request Handler Started - Port: " + port);
            ServerSocket socket = new ServerSocket(port);
            while (true) {
                log.info("It's time for the client requests!");
                Socket remote = socket.accept();
                log.info("Done.");
                executor.execute(new ServerHandler(remote));
            }
        } catch (IOException e) {
            log.error("ERROR: Problems with the server request handler.");
            log.error(e.getMessage());
        }
    }

    private static class ServerHandler implements Runnable {
        private final Socket socket;
        private final Marshaller marshaller = new Marshaller();

        public ServerHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            log.info("\n Server Handler Started - Socket: " + this.socket);
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                Message request = marshaller.demarshall(in);
                Response message = new Response();

                if (request.getType().equals(MessageType.ERROR)) {
                    log.warn("Server Handler could not interpret request.");
                    message.setHttpCode("400");
                    message.setHttpMessage("Bad Request");
                    message.setContent(request.getBody().toString());
                } else {
                    message = handleRequest(request);
                }

                String http_response = marshaller.marshall(message);
                out.write(http_response);

                out.close();
                in.close();
                socket.close();

            } catch (Exception e1) {
                log.error("ERROR: Failed to receive data from the handle requester.");
                e1.printStackTrace();
            }

            log.info("\n Server Handler Terminated - Socket: " + this.socket + "\n");

        }

        private Response handleRequest(Message intern_message) {
            try {
                Invoker invoker = new Invoker();
                return invoker.invokeRemoteObject(intern_message);
            } catch (Exception e) {
                log.error("ERROR: Failed to recover data from package.");
                JSONObject response = new JSONObject();
                response.append("ERROR: ", "An error ocurred when receiving the package.");
                return new Response("500", "Internal Server Error", response.toString());
            }
        }
    }
}
