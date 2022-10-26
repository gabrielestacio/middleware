package com.middleware.lifecycle_management;

import com.middleware.RemoteObject;

import java.util.concurrent.ConcurrentHashMap;

public class OtimizedPerRequestLifecycleManager implements LifecycleManager {

    // Maximum amount of instances
    private int max_pools;
    // Remote object mapping with its instance pool
    private ConcurrentHashMap<Object, Pool> pools;

    public OtimizedPerRequestLifecycleManager() {
        this.max_pools = 1;
        this.pools = new ConcurrentHashMap<>();
    }

    public int getMaxPools() {
        return this.max_pools;
    }

    public void setMaxPools(int max_pools) {
        this.max_pools = max_pools;
    }

    public void registerPerRequestInstancePool(RemoteObject remote_obj, int number_of_instances) {
        // Create pool
        Pool pool = new Pool(remote_obj, number_of_instances);
        // Register pool in the managed set
        this.pools.put(remote_obj.getId(), pool);
    }

    @Override
    public void registerRemoteObject(RemoteObject remote_obj) {
        // Create remote object pool
        this.registerPerRequestInstancePool(remote_obj, this.getMaxPools());
        // Publish the remote object to the middleware available pool
        LifecycleManagerRegistry.registerRemoteObject(remote_obj.getId(), Strategy.OPTIMIZED_PER_REQUEST_INSTANCE);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteobject_id) {
        // Get pool from remote object
        Pool pool = this.pools.get(remoteobject_id);
        // Get a servant
        RemoteObject servant = pool.getFreeInstance();
        // Remove servant from pool
        pool.removeFromPool(servant);
        // Activate servant
        servant.activate();
        // Return servant
        return servant;
    }

    @Override
    public void invocationDone(RemoteObject servant) {
        // Get pool from remote object
        Pool pool = this.pools.get(servant.getId());
        // Deactivate servant
        servant.deactivate();
        // Put servant back in poll
        pool.putBackToPool(servant);
    }

}
