package com.middleware.lifecycle_management;

import java.util.concurrent.ConcurrentHashMap;
import com.middleware.RemoteObject;

public class Static implements LifecycleManager {
    protected ConcurrentHashMap<Object, RemoteObject> remote_objects;

    public Static() {
        this.remote_objects = new ConcurrentHashMap<Object, RemoteObject>();
    }

    @Override
    public void newRemoteObject(RemoteObject remote_obj) {
        remote_obj.activate();
        this.remote_objects.put(remote_obj.getId(), remote_obj);
        LifecycleManagerStorage.newRemoteObject(remote_obj.getId(), Strategy.STATIC);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteobj_id) {
        return this.remote_objects.get(remoteobj_id);
    }

    @Override
    public void invocationDone(RemoteObject remote_obj){
    }
}