/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import org.epics.pvmanager.Function;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.SourceRateExpression;
import org.junit.Test;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import org.epics.pvmanager.expression.ChannelExpression;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguageTest {

    @Test
    public void vDouble1() {
        SourceRateExpression<VDouble> myPv = vDouble("my pv");
        assertThat(myPv.getName(), equalTo("my pv"));
    }

    @Test
    public void vAverageOf1() {
        DesiredRateExpression<VDouble> avgOfMyPV = averageOf(vDouble("my pv"));
        assertThat(avgOfMyPV.getName(), equalTo("avg(my pv)"));
    }

    @Test
    public void statisticsOf1() {
        DesiredRateExpression<VStatistics> statsOfMyPV = statisticsOf(vDouble("my pv"));
        assertThat(statsOfMyPV.getName(), equalTo("stats(my pv)"));
    }

    @Test
    public void vType1() {
        ChannelExpression<VType, Object> vType = vType("my pv");
        assertThat(vType.getName(), equalTo("my pv"));
        Function<VType> function = vType.getFunction();
        assertThat(function, instanceOf(ValueCache.class));
        assertThat(((ValueCache<VType>) function).getType(), equalTo(VType.class));
    }
}
