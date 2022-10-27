package com.middleware.lifecycle_management;

import java.util.concurrent.ConcurrentHashMap;
import com.middleware.RemoteObject;

public class PerRequest implements LifecycleManager {
    private ConcurrentHashMap<Object, RemoteObject> remote_objects;

    public PerRequest() {
        this.remote_objects = new ConcurrentHashMap<>();
    }

    @Override
    public void newRemoteObject(RemoteObject remote_obj) {
        this.remote_objects.put(remote_obj.getId(), remote_obj);
        LifecycleManagerStorage.newRemoteObject(remote_obj.getId(), Strategy.PER_REQUEST);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteobj_id) {
        RemoteObject remote_obj = this.remote_objects.get(remoteobj_id);
        RemoteObject object = new RemoteObject(remote_obj.getId(), remote_obj.getMethod());
        object.activate();
        return object;
    }

    @Override
    public void invocationDone(RemoteObject object) {
        object.deactivate();
    }

}
