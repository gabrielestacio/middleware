package com.middleware.lifecycle_management;

import java.util.concurrent.ConcurrentHashMap;
import com.middleware.RemoteObject;

public class OptimizedPerRequest implements LifecycleManager {
    private int maximum;
    private ConcurrentHashMap<Object, Pool> pools;

    public OptimizedPerRequest() {
        this.maximum = 1;
        this.pools = new ConcurrentHashMap<>();
    }

    public int getMaximum() {
        return this.maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public ConcurrentHashMap<Object, Pool> getPools(){
        return pools;
    }

    public void setPools(ConcurrentHashMap<Object, Pool> pools){
        this.pools = pools;
    }

    public void newPerRequestPool(RemoteObject remote_obj, int instances_qtt) {
        Pool pool = new Pool(remote_obj, instances_qtt);
        this.pools.put(remote_obj.getId(), pool);
    }

    @Override
    public void newRemoteObject(RemoteObject remote_obj) {
        this.newPerRequestPool(remote_obj, this.getMaximum());
        LifecycleManagerStorage.newRemoteObject(remote_obj.getId(), Strategy.OPTIMIZED_PER_REQUEST);
    }

    @Override
    public RemoteObject invocationArrived(Object id) {
        Pool pool = this.pools.get(id);
        RemoteObject servant = pool.getFreeInstance();
        pool.removeFromPool(servant);
        servant.activate();
        return servant;
    }

    @Override
    public void invocationDone(RemoteObject servant) {
        Pool pool = this.pools.get(servant.getId());
        servant.deactivate();
        pool.putBackToPool(servant);
    }
}
