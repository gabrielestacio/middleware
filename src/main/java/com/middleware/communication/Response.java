package com.middleware.communication;
public class Response {
    private String httpcode;
    private String httpmessage;
    private String content;

    public Response(){}

    public Response(String httpcode, String httpmessage, String content){
        this.httpcode = httpcode;
        this.httpmessage = httpmessage;
        this.content = content;
    }

    public String getHttpCode(){
        return httpcode;
    }

    public void setHttpCode(String httpcode){
        this.httpcode = httpcode;
    }

    public String getHttpMessage(){
        return httpmessage;
    }

    public void setHttpMessage(String httpmessage){
        this.httpmessage = httpmessage;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
}
