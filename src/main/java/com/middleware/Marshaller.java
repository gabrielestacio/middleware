package com.middleware;

import java.io.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import com.middleware.communication.Response;
import com.middleware.communication.MessageType;
import com.middleware.communication.Message;

@Slf4j
public class Marshaller {
    public Marshaller(){}
    
    public String marshall(Response message) {
        StringBuilder http_response = new StringBuilder();
        http_response.append("HTTP/1.1" + message.getHttpCode() + " " + message.getHttpMessage());
        http_response.append("\r\n" + "User-Agent: Medium" + "\r\n" + "Content-Type: application/json" + "\r\n" + "Content-Length:" + message.getContent().getBytes().length + "\r\n\r\n");
        http_response.append(message.getContent());
        return http_response.toString();
    }

    public Message demarshall(BufferedReader in) throws IOException {
        Message message = new Message();
        String line = in.readLine();

        if (!line.contains("HTTP")){
            log.warn("ERROR: Problems to read the request.");
            message.setType(MessageType.ERROR);
            return message;
        }

        String[] array = line.split(" ");
        message.setMethodType(array[0]);
        message.setRoute(array[1]);

        while ((line = in.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
        }

        StringBuilder builder = new StringBuilder();
        while(in.ready()){
            builder.append((char) in.read());
        }

        message.setBody(new JSONObject(builder.toString()));
        message.setType(MessageType.REQUEST);
        return message;
    }
}