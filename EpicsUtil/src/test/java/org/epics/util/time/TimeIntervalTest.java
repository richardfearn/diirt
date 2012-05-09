/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.time;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class TimeIntervalTest {

    public TimeIntervalTest() {
    }

    @Test
    public void interval1() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0));
        assertThat(interval.getStart(), equalTo(TimeStamp.of(0, 0)));
        assertThat(interval.getEnd(), equalTo(TimeStamp.of(3600, 0)));
    }

    @Test
    public void interval2() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(3600, 0), TimeStamp.of(7200, 0));
        assertThat(interval.getStart(), equalTo(TimeStamp.of(3600, 0)));
        assertThat(interval.getEnd(), equalTo(TimeStamp.of(7200, 0)));
    }

    @Test
    public void equals1() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0));
        assertThat(interval, equalTo(TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0))));
    }

    @Test
    public void equals2() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 1), TimeStamp.of(3600, 0));
        assertThat(interval, not(equalTo(TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0)))));
    }

    @Test
    public void equals3() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 1));
        assertThat(interval, not(equalTo(TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0)))));
    }
}