package com.middleware;

import java.lang.reflect.InvocationTargetException;
import com.middleware.communication.InternalMessage;
import com.middleware.communication.ResponseMessage;
import com.middleware.lifecycle_management.LifecycleManager;
import com.middleware.lifecycle_management.LifecycleManagerStorage;

public class Invoker {
    public Invoker(){}

    public ResponseMessage invokeRemoteObject(InternalMessage message) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        ResponseMessage response = null;
        String remoteobj_id = message.getReference();
        LifecycleManager lifecycle_manager = LifecycleManagerStorage.getLifecycleManager(remoteobj_id);
        
        if (lifecycle_manager == null) {
            response = LocationForwarder.delegate(remoteobj_id, message.getBody());
        }
        else {
            RemoteObject object = lifecycle_manager.invocationArrived(remoteobj_id);
            response = object.executeOperation(message.getBody());
            lifecycle_manager.invocationDone(object);
        }
        return response;
    }
}
