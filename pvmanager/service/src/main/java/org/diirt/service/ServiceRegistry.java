/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point for service lookup.
 * <p>
 * At startup, the ServiceLoader is used to load all available ServiceProviders
 * and register them to the default registry.
 *
 * @author carcassi
 */
public class ServiceRegistry {
    private static final Logger log = Logger.getLogger(ServiceRegistry.class.getName());
    private static final ServiceRegistry registry = new ServiceRegistry();

    /**
     * Returns the default service registry.
     * 
     * @return service registry, never null
     */
    public static ServiceRegistry getDefault() {
        return registry;
    }
    
    static {
        log.config("Fetching service providers");
        ServiceLoader<ServiceProvider> sl = ServiceLoader.load(ServiceProvider.class);
        int count = 0;
        for (ServiceProvider factory : sl) {
            log.log(Level.CONFIG, "Registering service provider ({0})", new Object[] {factory.getClass().getSimpleName()});
            registry.registerServices(factory);
            count++;
        }
        log.log(Level.CONFIG, "Registered {0} service providers", count);
    }
    
    private final Map<String, Service> services = new ConcurrentHashMap<>();

    /**
     * Registers a single service. Replaces previous instance of services
     * registered with the same name.
     * 
     * @param service a new service, can't be null
     */
    public void registerService(Service service) {
        services.put(service.getName(), service);
    }
    
    /**
     * Registers all the services from the given service provider.
     * 
     * @param serviceProvider a service provider, can't be null
     */
    public void registerServices(ServiceProvider serviceProvider) {
        for (Service service : serviceProvider.createServices()) {
            registerService(service);
        }
    }
    
    /**
     * Returns the names of all the registered services.
     * 
     * @return the name of all the registered services
     */
    public Set<String> getRegisteredServiceNames() {
        return Collections.unmodifiableSet(new HashSet<>(services.keySet()));
    }
    
    /**
     * Returns the service given the name.
     * 
     * @param name the name of the service to look up
     * @return the service or null if none was found
     */
    public Service findService(String name) {
        return services.get(name);
    }

    /**
     * Finds the service method given by the name of the service and the method.
     * 
     * @param serviceName the name of the service to look up
     * @param methodName the name of the method to look up
     * @return the service method, or null if none is found
     */
    public ServiceMethod findServiceMethod(String serviceName, String methodName) {
        Service service = findService(serviceName);
        if (service == null) {
            return null;
        }
        return service.getServiceMethods().get(methodName);
    }
    
    /**
     * Finds a service method using a lookup string of the format "service/method".
     * <p>
     * Currently, this method throws an exception if the service is not found.
     * It is not clear whether returning null would be more appropriate in 
     * that case.
     * 
     * @param fullName the service and method name for the lookup
     * @return the service
     */
    public ServiceMethod findServiceMethod(String fullName) {
        String[] tokens = fullName.split("/");
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Service method id must be \"service/method\"");
        }
        ServiceMethod method = findServiceMethod(tokens[0], tokens[1]);
        if (method == null) {
            throw new IllegalArgumentException("Service \"" + fullName + "\" not found");
        }
        return method;
    }
}
