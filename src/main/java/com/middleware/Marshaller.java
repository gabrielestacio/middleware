package com.middleware;

import java.io.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import com.middleware.communication.ResponseMessage;
import com.middleware.communication.Message;
import com.middleware.communication.InternalMessage;

@Slf4j
public class Marshaller {
    public Marshaller(){}
    
    public String marshall(ResponseMessage message) {
        StringBuilder http_response = new StringBuilder();
        http_response.append("HTTP/1.1 ");
        http_response.append(message.getHttpCode());
        http_response.append(" ");
        http_response.append(message.getHttpMessage());
        http_response.append("\r\nUser-Agent: Autumn\r\nContent-Type: application/json\r\nContent-Length:");
        http_response.append(message.getContent().getBytes().length);
        http_response.append("\r\n\r\n");
        http_response.append(message.getContent());
        return http_response.toString();
    }

    public InternalMessage unMarshall(BufferedReader in) throws IOException {
        InternalMessage message = new InternalMessage();
        String line = in.readLine();

        if (!line.contains("HTTP")){
            log.warn("ERROR: Problems to read the request.");
            message.setType(Message.ERROR);
            return message;
        }

        String[] parts = line.split(" ");
        message.setMethodType(parts[0]);
        message.setRoute(parts[1]);

        while ((line = in.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
        }

        StringBuilder payload = new StringBuilder();
        while(in.ready()){
            payload.append((char) in.read());
        }

        message.setBody(new JSONObject(payload.toString()));
        message.setType(Message.REQUEST);
        return message;
    }
}