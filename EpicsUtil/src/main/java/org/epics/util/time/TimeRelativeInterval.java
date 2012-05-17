/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.time;

/**
 *
 * @author carcassi
 */
public class TimeRelativeInterval {
    
    private final Object start;
    private final Object end;
    
    private TimeRelativeInterval(Object start, Object end) {
        this.start = start;
        this.end = end;
    }

    public static TimeRelativeInterval of(Timestamp start, Timestamp end) {
        return new TimeRelativeInterval(start, end);
    }
    
    public boolean isIntervalAbsolute() {
        return isStartAbsolute() && isEndAbsolute();
    }
    
    public boolean isStartAbsolute() {
        return start instanceof Timestamp || start == null;
    }
    
    public boolean isEndAbsolute() {
        return end instanceof Timestamp || end == null;
    }

    public Object getStart() {
        return start;
    }

    public Object getEnd() {
        return end;
    }

    public Timestamp getAbsoluteStart() {
        return (Timestamp) start;
    }
    
    public Timestamp getAbsoluteEnd() {
        return (Timestamp) end;
    }
    
    public TimeDuration getRelativeStart() {
        return (TimeDuration) start;
    }
    
    public TimeDuration getRelativeEnd() {
        return (TimeDuration) end;
    }
    
    public TimeInterval toAbsoluteInterval(Timestamp reference) {
        Timestamp absoluteStart;
        if (isStartAbsolute()) {
            absoluteStart = getAbsoluteStart();
        } else {
            absoluteStart = reference.plus(getRelativeStart());
        }
        Timestamp absoluteEnd;
        if (isEndAbsolute()) {
            absoluteEnd = getAbsoluteEnd();
        } else {
            absoluteEnd = reference.plus(getRelativeEnd());
        }
        return TimeInterval.between(absoluteStart, absoluteEnd);
    }
    
    
}
