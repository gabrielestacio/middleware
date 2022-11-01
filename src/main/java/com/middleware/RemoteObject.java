package com.middleware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import com.middleware.communication.Response;

@Slf4j
public class RemoteObject {
    private Object id;
    private Method method;
    private Object instance;

    public RemoteObject() {
    }

    public RemoteObject(Object id, Method method) {
        this.id = id;
        this.method = method;
    }

    public Object getId() {
        return this.id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getInstance() {
        return this.instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public void activate() {
        Class<?> clazz = this.method.getDeclaringClass();
        try {
            this.instance = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException instantiation_exception) {
            log.error(instantiation_exception.getMessage());
        } catch (IllegalAccessException illegal_access_exception) {
            log.error(illegal_access_exception.getMessage());
        } catch (InvocationTargetException invocation_target_exception) {
            log.error(invocation_target_exception.getMessage());
        } catch (NoSuchMethodException no_such_method_exception) {
            log.error(no_such_method_exception.getMessage());
        }
    }

    public void deactivate() {
        this.instance = null;
    }

    public Response executeOperation(JSONObject json_object) {
        try {
            JSONObject json = (JSONObject) this.method.invoke(this.instance, json_object);
            Response message = new Response("200", "OK", json.toString());
            return message;
        } catch (Exception e) {
            log.error(e.getMessage());
            JSONObject response = new JSONObject();
            response.append("ERROR: ", "An error occurred while processing the method.");
            return new Response("500", "Internal Server Error", response.toString());
        }
    }
}