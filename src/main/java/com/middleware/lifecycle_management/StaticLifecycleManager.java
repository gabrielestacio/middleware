package com.middleware.lifecycle_management;

import com.middleware.RemoteObject;

import java.util.concurrent.ConcurrentHashMap;

public class StaticLifecycleManager implements LifecycleManager {

    // Number of instances is predetermined
    protected ConcurrentHashMap<Object, RemoteObject> remote_objects;

    public StaticLifecycleManager() {
        this.remote_objects = new ConcurrentHashMap<Object, RemoteObject>();
    }

    @Override
    public void registerRemoteObject(RemoteObject remote_obj) {
        // Create servant
        remote_obj.activate();
        // Add remote object to managed set
        this.remote_objects.put(remote_obj.getId(), remote_obj);
        // Publish the remote object to the middleware available pool
        LifecycleManagerRegistry.registerRemoteObject(remote_obj.getId(), Strategy.STATIC_INSTANCE);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteobj_id) {
        // Return servant
        return this.remote_objects.get(remoteobj_id);
    }

}