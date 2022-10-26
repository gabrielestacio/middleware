package com.middleware.lifecycle_management;

import com.middleware.RemoteObject;

import java.util.concurrent.ConcurrentHashMap;

public class PerRequestLifecycleManager implements LifecycleManager {

    protected ConcurrentHashMap<Object, RemoteObject> remote_objects;

    public PerRequestLifecycleManager() {
        this.remote_objects = new ConcurrentHashMap<>();
    }

    @Override
    public void registerRemoteObject(RemoteObject remote_obj) {
        // Add remote object to managed set
        this.remote_objects.put(remote_obj.getId(), remote_obj);
        // Publish the remote object to the middleware available pool
        LifecycleManagerRegistry.registerRemoteObject(remote_obj.getId(), Strategy.PER_REQUEST_INSTANCE);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteobj_id) {
        // Get remote object
        RemoteObject remote_obj = this.remote_objects.get(remoteobj_id);
        // Create servant
        RemoteObject servant = new RemoteObject(remote_obj.getId(), remote_obj.getMethod());
        servant.activate();
        // Return servant
        return servant;
    }

    @Override
    public void invocationDone(RemoteObject servant) {
        // Destroy servant
        servant.deactivate();
    }

}
