/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;

/**
 *
 * @author carcassi
 */
public class GenericExecService extends Service {

    public GenericExecService() {
        super(new ServiceDescription("exec", "Command execution service")
                .addServiceMethod(new GenericExecServiceMethod()));
    }
    
}
