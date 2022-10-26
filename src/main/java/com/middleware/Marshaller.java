package com.middleware;


import com.middleware.communication.InternMessage;
import com.middleware.communication.MessageType;
import com.middleware.communication.ResponseMessage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;


import java.io.*;

/**
 * A MARSHALLER converts remote invocations into byte streams. The
 * MARSHALLER provides a generic mechanism that is not specific to any
 * particular remote object type.
 * The REQUESTOR, INVOKER, and REQUEST HANDLERS use the MARSHALLER
 * to retrieve the invocation information contained in the message byte
 * stream.
 */
@NoArgsConstructor
@Slf4j
public class Marshaller {

    /**
     * This method converts a ResponseMethod in an HTTP response string
     * @param message The ResponseMessage object with all necessary information
     *                to add in the HTTP response message.
     * @return A string as an HTTP message
     */
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

    /**
     * Parse an HTTP message receive by server in a InternMessage to
     * be used by all middleware components
     * @param in A BufferRead of socket
     * @return A InternMessage with all relevant information received by server
     * @throws IOException return an exception if an error occurs at read the socket
     */
    public InternMessage unmarshall(BufferedReader in) throws IOException {
    	// Instance to internal message
        InternMessage message = new InternMessage();
        // Read first line
        String line = in.readLine();
        // Checks if there is HTTP in the first line
        if (!line.contains("HTTP")){
            log.warn("Problems to read HTTP request");
            message.setType(MessageType.ERROR);
            return message;
        }
        //If there is, the header is separated.
        String[] parts = s.split(" ");
        message.setMethodType(parts[0]);
        message.setRoute(parts[1]);

        // read header
        while ((line = in.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
        }

        // read body
        StringBuilder payload = new StringBuilder();
        while(in.ready()){
            payload.append((char) in.read());
        }

        // setting message attributes
        message.setBody(new JSONObject(payload.toString()));
        message.setType(MessageType.REQUEST);
        return message;
    }

}
