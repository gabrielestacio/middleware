package com.middleware.lifecycle_management;

import java.util.HashMap;

public class LifecycleManagerStorage {
    private static HashMap<Strategy, LifecycleManager> lifecycle_managers = new HashMap<Strategy, LifecycleManager>();
    private static HashMap<Object, Strategy> remote_objects = new HashMap<Object, Strategy>();

    public LifecycleManagerStorage(){}

    public static LifecycleManager getLifecycleManager(Strategy strategy) {
        return lifecycle_managers.get(strategy);
    }

    public static LifecycleManager getLifecycleManager(Object remoteobj_id) {
        return lifecycle_managers.get(remote_objects.get(remoteobj_id));
    }

    public static void newLifecycleManager(Strategy strategy, LifecycleManager lifecycle_manager) {
        lifecycle_managers.put(strategy, lifecycle_manager);
    }

    public static void newRemoteObject(Object remoteobj_id, Strategy strategy) {
        remote_objects.put(remoteobj_id, strategy);
    }

}