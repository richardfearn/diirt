/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class ServiceMethodTest {
    
    public ServiceMethodTest() {
    }

    @Test
    public void execute1() {
        ServiceMethod method = new AddServiceMethod();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", 1);
        parameters.put("arg2", 2);
        ValueCache<Map<String, Object>> cache = new ValueCache<>();
        ValueCache<Exception> exceptionCache = new ValueCache<>();
        method.execute(parameters, cache, exceptionCache);
        
        assertThat(cache.get().get("result"), equalTo((Object) 3.0));
        assertThat(exceptionCache.get(), nullValue());
    }

    @Test
    public void execute2() {
        ServiceMethod method = new AddServiceMethod();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", 1);
        parameters.put("arg2", "test");
        ValueCache<Map<String, Object>> cache = new ValueCache<>();
        ValueCache<Exception> exceptionCache = new ValueCache<>();
        method.execute(parameters, cache, exceptionCache);
        
        assertThat(cache.get(), nullValue());
        assertThat(exceptionCache.get(), instanceOf(IllegalArgumentException.class));
    }

    @Test
    public void execute3() {
        ServiceMethod method = new MultiplyServiceMethod();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", 1);
        parameters.put("arg2", 2);
        ValueCache<Map<String, Object>> cache = new ValueCache<>();
        ValueCache<Exception> exceptionCache = new ValueCache<>();
        method.execute(parameters, cache, exceptionCache);
        
        assertThat(cache.get().get("result"), equalTo((Object) 2.0));
        assertThat(exceptionCache.get(), nullValue());
    }
    
    @Test
    public void toString1() {
        ServiceMethod method = new MultiplyServiceMethod();
        assertThat(method.toString(), equalTo("multiply(Number arg1, Number arg2): Number result"));
    }
    
    @Test
    public void toString2() {
        ServiceMethod method = new AddServiceMethod();
        assertThat(method.toString(), equalTo("add(Number arg1, Number arg2): Number result"));
    }
}