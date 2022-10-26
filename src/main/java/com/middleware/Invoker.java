package com.middleware;


import java.lang.reflect.InvocationTargetException;

import com.middleware.communication.InternMessage;
import com.middleware.communication.ResponseMessage;
import com.middleware.lifecycle_management.LifecycleManager;
import com.middleware.lifecycle_management.LifecycleManagerRegistry;

/**
 * Provide an INVOKER that accepts client invocations from REQUESTORS.
 * REQUESTORS send requests across the network, containing the ID of
 * the remote object, operation name, operation parameters, as well as
 * additional contextual information. The INVOKER reads the request
 * and demarshals it to obtain the OBJECT ID and the name of the operation.
 * It then dispatches the invocation with demarshaled invocation
 * parameters to the targeted remote object. That is, it looks up the
 * correct local object and its operation implementation, as described by
 * the remote invocation, and invokes it.
 */
public class Invoker {
    // Method that invokes a remote object, receiving an InternMessage and returning a ResponseMessage
    public ResponseMessage invokeRemoteObject(InternalMessage message) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        ResponseMessage response = null;
        // Forming the remote object id
        String remoteobj_id = message.getReference();
        // Search for cycle manager responsible for remote object
        LifecycleManager lifecycle_manager = LifecycleManagerRegistry.getLifecycleManager(remoteobj_id);
        /**
         * In the event that a target remote object cannot be found by the INVOKER,
         * the INVOKER can delegate dispatching to a LOCATION FORWARDER .
         */
        if (lifecycle_manager == null) {
            response = LocationForwarder.delegate(remoteobj_id, message.getBody());
        }
        else {
            // Seek remote object
            RemoteObject servant = lifecycle_manager.invocationArrived(remoteobj_id);
            // Calls the invoke method passing data.
            response = servant.executeOperation(message.getBody());
            // Release remote object
            lifecycle_manager.invocationDone(servant);
        }
        return response;
    }
}
