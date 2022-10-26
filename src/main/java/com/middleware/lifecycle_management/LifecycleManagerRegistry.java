package com.middleware.lifecycle_management;

import java.util.HashMap;

/**
 * The LifecycleManagerRegistry class keeps track of all LIFECYCLE MANAGERS
 * in the system, as well as their relationship to remote objects and their servants.
 */
public class LifecycleManagerRegistry {

    private static HashMap<Strategy, LifecycleManager> lifecycle_managers = new HashMap<Strategy, LifecycleManager>();
    private static HashMap<Object, Strategy> remote_objects = new HashMap<Object, Strategy>();

    public static LifecycleManager getLifecycleManager(Strategy strategy) {
        return lifecycle_managers.get(strategy);
    }

    public static LifecycleManager getLifecycleManager(Object remoteobj_id) {
        return lifecycle_managers.get(remote_objects.get(remoteobj_id));
    }

    public static void registerLifecycleManager(Strategy strategy, LifecycleManager lifecycle_manager) {
        lifecycleManagers.put(strategy, lifecycle_manager);
    }

    public static void registerRemoteObject(Object remoteobj_id, Strategy strategy) {
        remoteObjects.put(remoteobj_id, strategy);
    }

}