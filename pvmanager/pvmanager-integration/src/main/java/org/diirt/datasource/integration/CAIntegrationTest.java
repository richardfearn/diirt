/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import java.util.Arrays;
import java.util.List;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.ca.JCADataSource;
import org.diirt.datasource.ca.JCADataSourceBuilder;

/**
 *
 * @author carcassi
 */
public class CAIntegrationTest {
    public static void main(String[] args) {
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        List<TestPhase> phases = Arrays.<TestPhase>asList(new UpdateTestPhase(),
                new RestartTestPhase(),
                new OutageTestPhase(),
                new TypeChangeTestPhase(),
                new RepeatedDisconnectTestPhase());
        int debugLevel = 1;
        
        PVManager.setDefaultDataSource(new JCADataSource());
        
        for (TestPhase phase : phases) {
            phase.setDebugLevel(debugLevel);
            phase.execute();
        }
        
        PVManager.getDefaultDataSource().close();
        
        PVManager.setDefaultDataSource(new JCADataSourceBuilder().dbePropertySupported(true).build());

        phases = Arrays.<TestPhase>asList(new UpdateTestPhase(),
                new RestartTestPhase(),
                new OutageTestPhase(),
                new TypeChangeTestPhase(),
                new RepeatedDisconnectTestPhase());
        
        for (TestPhase phase : phases) {
            phase.setDebugLevel(debugLevel);
            phase.execute();
        }
        
        PVManager.getDefaultDataSource().close();
    }
}
