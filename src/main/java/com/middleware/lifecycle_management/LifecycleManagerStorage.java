package com.middleware.lifecycle_management;

import java.util.HashMap;

public class LifecycleManagerStorage {
    private static HashMap<Strategy, LifecycleManager> lm_storage = new HashMap<Strategy, LifecycleManager>();
    private static HashMap<Object, Strategy> ro_storage = new HashMap<Object, Strategy>();

    public LifecycleManagerStorage(){}

    public static LifecycleManager getLifecycleManager(Strategy strategy) {
        return lm_storage.get(strategy);
    }

    public static LifecycleManager getLifecycleManager(Object remoteobj_id) {
        return lm_storage.get(ro_storage.get(remoteobj_id));
    }

    public static void newLifecycleManager(Strategy strategy, LifecycleManager lifecycle_manager) {
        lm_storage.put(strategy, lifecycle_manager);
    }

    public static void newRemoteObject(Object remoteobj_id, Strategy strategy) {
        ro_storage.put(remoteobj_id, strategy);
    }

}