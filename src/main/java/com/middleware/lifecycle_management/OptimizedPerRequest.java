package com.middleware.lifecycle_management;

import java.util.concurrent.ConcurrentHashMap;
import com.middleware.RemoteObject;

public class OptimizedPerRequest implements LifecycleManager {
    private int max_pools;
    private ConcurrentHashMap<Object, Pool> pools;

    public OptimizedPerRequest() {
        this.max_pools = 1;
        this.pools = new ConcurrentHashMap<>();
    }

    public int getMaxPools() {
        return this.max_pools;
    }

    public void setMaxPools(int max_pools) {
        this.max_pools = max_pools;
    }

    public ConcurrentHashMap<Object, Pool> getPools(){
        return pools;
    }

    public void setPools(ConcurrentHashMap<Object, Pool> pools){
        this.pools = pools;
    }

    public void newPerRequestPool(RemoteObject remote_obj, int number_of_instances) {
        Pool pool = new Pool(remote_obj, number_of_instances);
        this.pools.put(remote_obj.getId(), pool);
    }

    @Override
    public void newRemoteObject(RemoteObject remote_obj) {
        this.newPerRequestPool(remote_obj, this.getMaxPools());
        LifecycleManagerStorage.newRemoteObject(remote_obj.getId(), Strategy.OPTIMIZED_PER_REQUEST);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteobject_id) {
        Pool pool = this.pools.get(remoteobject_id);
        RemoteObject object = pool.getFreeInstance();
        pool.removeFromPool(object);
        object.activate();
        return object;
    }

    @Override
    public void invocationDone(RemoteObject object) {
        Pool pool = this.pools.get(object.getId());
        object.deactivate();
        pool.putBackToPool(object);
    }
}
