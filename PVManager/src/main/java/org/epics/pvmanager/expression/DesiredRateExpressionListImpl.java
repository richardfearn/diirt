/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A list of desired rate expression, to have functions that work on multiple
 * expressions at the same time.
 *
 * @author carcassi
 */
public class DesiredRateExpressionListImpl<T> implements DesiredRateExpressionList<T> {
    
    private List<DesiredRateExpression<T>> desiredRateExpressions;

    public DesiredRateExpressionListImpl() {
        this.desiredRateExpressions = new ArrayList<DesiredRateExpression<T>>();
    }

    DesiredRateExpressionListImpl(Collection<? extends DesiredRateExpression<T>> desiredRateExpressions) {
        this.desiredRateExpressions = new ArrayList<DesiredRateExpression<T>>(desiredRateExpressions);
    }
    
    @Override
    public DesiredRateExpressionListImpl<T> and(DesiredRateExpressionList<T> expressions) {
        desiredRateExpressions.addAll(expressions.getDesiredRateExpressions());
        return this;
    }

    @Override
    public List<DesiredRateExpression<T>> getDesiredRateExpressions() {
        return desiredRateExpressions;
    }
    
}
