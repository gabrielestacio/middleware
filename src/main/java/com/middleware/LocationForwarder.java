package com.middleware;


import com.middleware.communication.ResponseMessage;
import org.json.JSONObject;

public class LocationForwarder {

    public static ResponseMessage delegate(Object remoteobj_id, JSONObject json) {
        JSONObject response = new JSONObject();
        response.append("Error: ", "Method not found");
        return new ResponseMessage("404", "Not Found", response.toString());
    }

}
