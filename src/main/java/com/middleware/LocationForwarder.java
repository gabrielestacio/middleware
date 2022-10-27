package com.middleware;

import org.json.JSONObject;
import com.middleware.communication.ResponseMessage;

public class LocationForwarder {
    public LocationForwarder(){}

    public static ResponseMessage delegate(Object remoteobj_id, JSONObject json) {
        JSONObject response = new JSONObject();
        response.append("ERROR: ", "Method not found.");
        return new ResponseMessage("404", "Not Found", response.toString());
    }

}
