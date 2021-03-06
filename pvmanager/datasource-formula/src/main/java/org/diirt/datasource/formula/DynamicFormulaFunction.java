/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import org.diirt.datasource.PVDirector;

/**
 * Formula function that can add and remove dynamically access to
 * pvs.
 * <p>
 * This formula function is given a director which can be used to open/close
 * expressions that read real-time data.
 *
 * @author carcassi
 */
public abstract class DynamicFormulaFunction extends StatefulFormulaFunction {
    
    private PVDirector<?> director;

    /**
     * The director to use to connect/disconnect live data expressions.
     * 
     * @return the director
     */
    public final PVDirector<?> getDirector() {
        return director;
    }
    
    /**
     * Changes the director. This is not part of the public API: the director
     * is set by the infrastructure.
     * 
     * @param director the new director
     */
    void setDirector(PVDirector<?> director) {
        this.director = director;
    }
    
}
