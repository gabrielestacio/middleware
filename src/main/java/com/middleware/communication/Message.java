package com.middleware.communication;

import org.json.JSONObject;

public class Message {
    private String route;
    private String method_type;
    private JSONObject body;
    private MessageType type;

    public Message(){}

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

    public MessageType getType(){
        return type;
    }

    public void setType(MessageType type){
        this.type = type;
    }

    public String buildID() {
        return this.getMethodType().toLowerCase() + this.getRoute();
    }
}
