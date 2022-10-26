package com.middleware;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import com.middleware.annotations.*;
import com.middleware.annotations.Pool;
import com.middleware.lifecycle_management.*;

/*
 * Class that encapsulates the middleware, responsible
 *  for storing the methods in hashmaps and starting the
 *  server on the correct port.
 */
/*Classe principal do Middleware */
public class Medium {

    public Medium() {
        LifecycleManagerRegistry.registerLifecycleManager(Strategy.STATIC_INSTANCE, new StaticLifecycleManager());
        LifecycleManagerRegistry.registerLifecycleManager(Strategy.OPTIMIZED_STATIC_INSTANCE, new OtimizedStaticLifecycleManager());
        LifecycleManagerRegistry.registerLifecycleManager(Strategy.PER_REQUEST_INSTANCE, new PerRequestLifecycleManager());
        LifecycleManagerRegistry.registerLifecycleManager(Strategy.OPTIMIZED_PER_REQUEST_INSTANCE, new OtimizedPerRequestLifecycleManager());
    }

    // calls the method that filters and saves remote objects
    public void registerRemoteObjects(Object object) {
        //	Extract the component
        Class<?> clazz = object.getClass();
        LifecycleManager lifecycle_manager = null;
        for (Method method : clazz.getDeclaredMethods()) {
            lifecycle_manager = this.filterLifecycle(method);
            RemoteObject remote_object = new RemoteObject();
            if (method.isAnnotationPresent(Get.class)) {
                remoteO_oject.setId("get" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Get.class).router());
            } else if (method.isAnnotationPresent(Post.class)) {
                remote_object.setId("post" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Post.class).router());
            } else if (method.isAnnotationPresent(Put.class)) {
                remote_object.setId("put" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Put.class).router());
            } else if (method.isAnnotationPresent(Delete.class)) {
                remote_object.setId("delete" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Delete.class).router());
            }
            method.setAccessible(true);
            remote_object.setMethod(method);
            lifecycle_manager.registerRemoteObject(remote_object);
        }
    }

    public LifecycleManager filterLifecycle(Method method) {
        Lifecycle lifecycle = method.getAnnotation(Lifecycle.class);
        if (lifecycle == null) {
            return LifecycleManagerRegistry.getLifecycleManager(Strategy.STATIC_INSTANCE);
        } else {
            LifecycleManager lifecycle_manager = LifecycleManagerRegistry.getLifecycleManager(lifecycle.strategy());
            if (lifecycle_manager instanceof OtimizedPerRequestLifecycleManager) {
                Pool pool = method.getAnnotation(Pool.class);
                if (pool != null) {
                    ((OtimizedPerRequestLifecycleManager) lifecycle_manager).setMaxPools(pool.maxQuantity());
                }
            }
            return lifecycle_manager;
        }
    }

    //	Method that starts the server on the chosen port
    public void start(int port) {
        // ServerRequestHandler instance on the chosen port
        ServerRequestHandler server = new ServerRequestHandler(port);
        // call start method
        server.run();
    }
}
