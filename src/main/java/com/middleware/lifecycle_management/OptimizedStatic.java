package com.middleware.lifecycle_management;

import com.middleware.RemoteObject;

public class OptimizedStatic extends Static {
    public OptimizedStatic() {}

    @Override
    public void newRemoteObject(RemoteObject remote_obj) {
        this.remote_objects.put(remote_obj.getId(), remote_obj);
        LifecycleManagerStorage.newRemoteObject(remote_obj.getId(), Strategy.OPTIMIZED_STATIC);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteobj_id) {
        RemoteObject remote_obj = this.remote_objects.get(remoteobj_id);
        if (remote_obj.getInstance() == null) {
            remote_obj.activate();
        }
        return remote_obj;
    }
}