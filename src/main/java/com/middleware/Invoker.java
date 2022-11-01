package com.middleware;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONObject;

import com.middleware.communication.Message;
import com.middleware.communication.Response;
import com.middleware.lifecycle_management.LifecycleManager;
import com.middleware.lifecycle_management.LifecycleManagerStorage;

public class Invoker {
    public Invoker(){}

    public Response invokeRemoteObject(Message message) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        Response response = null;
        String remoteobj_id = message.buildID();
        LifecycleManager lifecycle_manager = LifecycleManagerStorage.getLifecycleManager(remoteobj_id);
        
        if (lifecycle_manager == null) {
            JSONObject json = new JSONObject();
            json.append("ERROR: ", "Method not found.");
            response = new Response("404", "Not Found", json.toString());
        }
        else {
            RemoteObject object = lifecycle_manager.invocationArrived(remoteobj_id);
            response = object.executeOperation(message.getBody());
            lifecycle_manager.invocationDone(object);
        }
        return response;
    }
}
