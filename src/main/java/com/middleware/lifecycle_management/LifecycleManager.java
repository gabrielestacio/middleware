package com.middleware.lifecycle_management;

import com.middleware.RemoteObject;

public interface LifecycleManager {

    public void registerRemoteObject(RemoteObject remote_object);

    public RemoteObject invocationArrived(Object remoteobj_id);

    public default void invocationDone(RemoteObject remote_object) {}

}
