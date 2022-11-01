package com.middleware;

import java.lang.reflect.Method;
import com.middleware.annotations.*;
import com.middleware.annotations.Pool;
import com.middleware.lifecycle_management.*;

public class Medium {
    public Medium() {
        LifecycleManagerStorage.newLifecycleManager(Strategy.STATIC, new Static());
        LifecycleManagerStorage.newLifecycleManager(Strategy.OPTIMIZED_STATIC, new OptimizedStatic());
        LifecycleManagerStorage.newLifecycleManager(Strategy.PER_REQUEST, new PerRequest());
        LifecycleManagerStorage.newLifecycleManager(Strategy.OPTIMIZED_PER_REQUEST, new OptimizedPerRequest());
    }

    public void newRemoteObjects(Object object) {
        Class<?> clazz = object.getClass();
        LifecycleManager lifecycle_manager = null;
        for (Method method : clazz.getDeclaredMethods()) {
            lifecycle_manager = this.getLifecycle(method);
            RemoteObject remote_object = new RemoteObject();
            if (method.isAnnotationPresent(Get.class)) {
                remote_object.setId("get" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Get.class).router());
            } else if (method.isAnnotationPresent(Post.class)) {
                remote_object.setId("post" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Post.class).router());
            } else if (method.isAnnotationPresent(Put.class)) {
                remote_object.setId("put" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Put.class).router());
            } else if (method.isAnnotationPresent(Delete.class)) {
                remote_object.setId("delete" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Delete.class).router());
            } else if (method.isAnnotationPresent(Delete.class)) {
                remote_object.setId("patch" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Patch.class).router());
            }
            method.setAccessible(true);
            remote_object.setMethod(method);
            lifecycle_manager.newRemoteObject(remote_object);
        }
    }

    public LifecycleManager getLifecycle(Method method) {
        Lifecycle lifecycle = method.getAnnotation(Lifecycle.class);
        if (lifecycle == null) {
            return LifecycleManagerStorage.getLifecycleManager(Strategy.STATIC);
        } else {
            LifecycleManager lifecycle_manager = LifecycleManagerStorage.getLifecycleManager(lifecycle.strategy());
            if (lifecycle_manager instanceof OptimizedPerRequest) {
                Pool pool = method.getAnnotation(Pool.class);
                if (pool != null) {
                    ((OptimizedPerRequest) lifecycle_manager).setMaximum(pool.maximum());
                }
            }
            return lifecycle_manager;
        }
    }

    public void start(int port) {
        ServerRequestHandler server = new ServerRequestHandler(port);
        server.run();
    }
}
