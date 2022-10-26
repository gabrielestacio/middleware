package com.middleware.lifecycle_management;

import com.middleware.RemoteObject;

public class OtimizedStaticLifecycleManager extends StaticLifecycleManager {

    public OtimizedStaticLifecycleManager() {
        super();
    }

    @Override
    public void registerRemoteObject(RemoteObject remote_obj) {
        // Add remote object to managed set
        this.remote_objects.put(remote_obj.getId(), remote_obj);
        // Publish the remote object to the middleware available pool
        LifecycleManagerRegistry.registerRemoteObject(remote_obj.getId(), Strategy.OPTIMIZED_STATIC_INSTANCE);
    }

    // Used the pattern of LAZY ACQUISITION
    @Override
    public RemoteObject invocationArrived(Object remoteobj_id) {
        // Get remote object
        RemoteObject remote_obj = this.remote_objects.get(remoteobj_id);
        // Check if there is already servant
        if (remote_obj.getInstance() == null) {
            // Create servant
            remote_obj.activate();
        }
        // Return servant
        return remote_obj;
    }

}