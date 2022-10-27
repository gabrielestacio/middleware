package com.middleware.communication;

import org.json.JSONObject;

public class InternalMessage {
    private String route;
    private String method_type;
    private JSONObject body;
    private Message type;

    public InternalMessage(){}

    public String getReference() {
        return this.getMethodType().toLowerCase() + this.getRoute();
    }

    public String getRoute(){
        return route;
    }

    public void setRoute(String route){
        this.route = route;
    }

    public String getMethodType(){
        return method_type;
    }

    public void setMethodType(String method_type){
        this.method_type = method_type;
    }

    public JSONObject getBody(){
        return body;
    }

    public void setBody(JSONObject body){
        this.body = body;
    }

    public Message getType(){
        return type;
    }

    public void setType(Message type){
        this.type = type;
    }
}
