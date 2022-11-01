package com.middleware.lifecycle_management;

import com.middleware.RemoteObject;

public interface LifecycleManager {
    public void newRemoteObject(RemoteObject remote_object);
    public RemoteObject invocationArrived(Object id);
    public void invocationDone(RemoteObject remote_object);

}
