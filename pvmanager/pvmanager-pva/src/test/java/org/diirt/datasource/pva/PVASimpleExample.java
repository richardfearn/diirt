/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pva;

import static org.diirt.datasource.ExpressionLanguage.channel;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.util.time.TimeDuration;

public class PVASimpleExample {
	
	public static void main(String[] args) throws InterruptedException
	{
		PVManager.setDefaultDataSource(new PVADataSource());
		PVReader<Object> reader = PVManager.read(channel("testCounter")).
				readListener(new PVReaderListener<Object>() {

					@Override
					public void pvChanged(PVReaderEvent<Object> event) {
						if (event.isValueChanged())
							System.out.println(event.getPvReader().getValue());
						else
							System.out.println(event.toString());
						
					}
				}).maxRate(TimeDuration.ofHertz(10));
		
		// forever
		while (System.currentTimeMillis() != 0)
			Thread.sleep(Long.MAX_VALUE);
		
		reader.close();
	}

}
