/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.loc;

import org.epics.pvmanager.*;
import org.epics.pvmanager.data.VDouble;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

/**
 *
 * @author carcassi
 */
public class LocChannelHandlerTest {

    public LocChannelHandlerTest() {
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Mock ValueCache<VDouble> vDoubleCache1;
    @Mock ValueCache<VDouble> vDoubleCache2;
    @Mock WriteFunction<Boolean> vDoubleConnCache1;
    @Mock WriteFunction<Boolean> vDoubleConnCache2;
    @Mock ChannelWriteCallback channelWriteCallback;
    @Mock WriteFunction<Exception> exceptionHandler;
    @Mock ValueCache<Boolean> vDoubleWriteConnCache1;

    @Test
    public void writeToLocalChannelSingleMonitor() {
        
        // Creating a test local channel
        LocalChannelHandler channel = new LocalChannelHandler("test1");
        assertThat(channel.getChannelName(), equalTo("test1"));
        assertThat(channel.getUsageCounter(), equalTo(0));
        assertThat(channel.isConnected(), is(false));

        // Attaching a monitor cache/collector
        ChannelHandlerReadSubscription readSubscription = new ChannelHandlerReadSubscription(vDoubleCache1, exceptionHandler, vDoubleConnCache1);
        channel.addReader(readSubscription);
        assertThat(channel.getUsageCounter(), equalTo(1));
        assertThat(channel.isConnected(), is(true));

        // Adding a writer
        WriteCache<?> cache = new WriteCache<Object>();
        ChannelHandlerWriteSubscription writeSubscription =
                new ChannelHandlerWriteSubscription(cache, exceptionHandler, vDoubleWriteConnCache1);
        channel.addWriter(writeSubscription);
        assertThat(channel.getUsageCounter(), equalTo(2));
        assertThat(channel.isConnected(), is(true));

        // Writing a number and see if it is converted to a VDouble
        channel.write(6.28, channelWriteCallback);
        
        // Removing all readers and writers
        channel.removeReader(readSubscription);
        channel.removeWrite(writeSubscription);
        assertThat(channel.getUsageCounter(), equalTo(0));
        assertThat(channel.isConnected(), is(false));
        
        InOrder inOrder = inOrder(vDoubleCache1, channelWriteCallback, exceptionHandler);
        ArgumentCaptor<VDouble> newValue = ArgumentCaptor.forClass(VDouble.class); 
        inOrder.verify(vDoubleCache1).setValue(newValue.capture());
        assertThat(newValue.getValue().getValue(), equalTo(6.28));
        inOrder.verify(channelWriteCallback).channelWritten(null);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void writeToLocalChannelTwoMonitors() {
        
        // Creating a test local channel
        LocalChannelHandler channel = new LocalChannelHandler("test2", 0.0);
        assertThat(channel.getChannelName(), equalTo("test2"));
        assertThat(channel.getUsageCounter(), equalTo(0));
        assertThat(channel.isConnected(), is(false));

        // Attaching a monitor cache/collector
        ChannelHandlerReadSubscription readSubscription1 = new ChannelHandlerReadSubscription(vDoubleCache1, exceptionHandler, vDoubleConnCache1);
        channel.addReader(readSubscription1);
        assertThat(channel.getUsageCounter(), equalTo(1));
        assertThat(channel.isConnected(), is(true));
        ChannelHandlerReadSubscription readSubscription2 = new ChannelHandlerReadSubscription(vDoubleCache2, exceptionHandler, vDoubleConnCache2);

        // Attaching a monitor cache/collector
        channel.addReader(readSubscription2);
        assertThat(channel.getUsageCounter(), equalTo(2));
        assertThat(channel.isConnected(), is(true));

        // Adding a writer
        WriteCache<?> cache = new WriteCache<Object>();
        ChannelHandlerWriteSubscription writeSubscription = new ChannelHandlerWriteSubscription(cache, exceptionHandler, vDoubleWriteConnCache1);
        channel.addWriter(writeSubscription);
        assertThat(channel.getUsageCounter(), equalTo(3));
        assertThat(channel.isConnected(), is(true));

        // Writing a number and see if it is converted to a VDouble
        channel.write(16.28, channelWriteCallback);
        
        // Remove reader/writers
        channel.removeWrite(new ChannelHandlerWriteSubscription(cache, exceptionHandler, vDoubleWriteConnCache1));
        channel.removeReader(readSubscription1);
        channel.removeReader(readSubscription2);
        assertThat(channel.getUsageCounter(), equalTo(0));
        assertThat(channel.isConnected(), is(false));
        
        ArgumentCaptor<VDouble> newValue = ArgumentCaptor.forClass(VDouble.class); 
        verify(vDoubleCache1, times(2)).setValue(newValue.capture());
        assertThat(newValue.getAllValues().get(0).getValue(), equalTo(0.0));
        assertThat(newValue.getAllValues().get(1).getValue(), equalTo(16.28));
        ArgumentCaptor<VDouble> newValue2 = ArgumentCaptor.forClass(VDouble.class); 
        verify(vDoubleCache2, times(2)).setValue(newValue2.capture());
        assertThat(newValue2.getAllValues().get(0).getValue(), equalTo(0.0));
        assertThat(newValue2.getAllValues().get(1).getValue(), equalTo(16.28));
        verify(channelWriteCallback).channelWritten(null);
        verifyZeroInteractions(channelWriteCallback, exceptionHandler);
    }
}
