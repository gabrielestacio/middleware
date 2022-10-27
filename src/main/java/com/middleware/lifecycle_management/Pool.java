package com.middleware.lifecycle_management;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.middleware.RemoteObject;

public class Pool {
    private List<RemoteObject> instances;
    private Lock lock;
    private Condition is_empty;

    public Pool(RemoteObject remoteObject, int number) {
        this.instances = new ArrayList<RemoteObject>();
        for (int i = 0; i < number; i++) {
            this.addPoolInstance(remoteObject);
        }
        this.lock = new ReentrantLock(true);
        this.is_empty = this.lock.newCondition();
    }

    public void addPoolInstance(RemoteObject remoteObject) {
        this.instances.add(new RemoteObject(remoteObject.getId(), remoteObject.getMethod()));
    }

    public RemoteObject getFreeInstance() {
        RemoteObject remote_object = null;
        this.lock.lock();
        try {
            while(this.instances.size() == 0) {
                is_empty.await();
            }
            remote_object = instances.get(0);
        } catch (InterruptedException interrupted_exception) {
            interrupted_exception.printStackTrace();
        } finally {
            lock.unlock();
        }
        return remote_object;
    }

    public void removeFromPool(RemoteObject remoteObject) {
        this.lock.lock();
        try {
            this.instances.remove(remoteObject);
        } finally {
            this.lock.unlock();
        }
    }

    public void putBackToPool(RemoteObject remoteObject) {
        this.lock.lock();
        try {
            this.instances.add(remoteObject);
            this.is_empty.signal();
        } finally {
            this.lock.unlock();
        }
    }

}