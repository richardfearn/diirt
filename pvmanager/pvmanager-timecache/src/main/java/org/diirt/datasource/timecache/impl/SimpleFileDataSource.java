/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.source.SourceData;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.Display;
import org.diirt.vtype.Time;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;

/**
 * {@link DataSource} implementation which read samples from a file dump of
 * 'sample_view' from Archive RDB.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class SimpleFileDataSource implements DataSource {

	private static final Logger log = Logger.getLogger(SimpleFileDataSource.class.getName());

	private final String csvFile;
	private final String csvSplitBy = ";";

	private static final Integer channel_name = 0;
	private static final Integer smpl_time = 1;
	private static final Integer nanosecs = 2;
	private static final Integer severity = 3;
	// private static final Integer status = 4;
	private static final Integer num_val = 5;
	private static final Integer float_val = 6;
	private static final Integer str_val = 7;
	private static final Integer array_nval = 8;
	private static final Integer array_val = 9;

	private TreeMap<Timestamp, Integer> indexes;

	public SimpleFileDataSource(String csvFilePath) {
		this.csvFile = csvFilePath;
		indexes = new TreeMap<Timestamp, Integer>();
	}

	/** {@inheritDoc} */
	@Override
	public DataChunk getData(String channelName, Timestamp from) {
		if (channelName == null || channelName.isEmpty() || from == null)
			return new DataChunk();
		try {
			return readSamples(channelName.trim(), from);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		return new DataChunk();
	}

	private DataChunk readSamples(String channelName, Timestamp from)
			throws Exception {
		DataChunk chunk = new DataChunk();

		BufferedReader br = null;
		String currentLine = "";
		Timestamp lastIndex = indexes.floorKey(from);
		Integer lineToStart = lastIndex == null ? 1 : indexes.get(lastIndex);
		Integer lineNumber = -1;
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((currentLine = br.readLine()) != null) {
				lineNumber++;
				if (lineNumber < lineToStart)
					continue;
				String[] columns = getColumns(currentLine);
				if (columns[channel_name] != null
						&& columns[channel_name].equals(channelName)) {
					// Get time stamp
					final java.sql.Timestamp stamp = java.sql.Timestamp
							.valueOf(columns[smpl_time]);
					stamp.setNanos(Integer.valueOf(columns[nanosecs]));
					final Timestamp time = fromSQLTimestamp(stamp);
					if (time.compareTo(from) >= 0) {
						final VType value = decodeValue(columns, time);
						SourceData data = new SourceData(time, value);
						if (!chunk.add(data)) {
							TimeInterval i = chunk.getInterval();
							if (i != null)
								indexes.put(i.getEnd(), lineNumber);
							break;
						}
					}
				}
				lineNumber++;
			}
		} finally {
			if (br != null)
				br.close();
		}
		return chunk;
	}

	private VType decodeValue(final String[] columns, final Timestamp ts) throws Exception {
		Time time = ValueFactory.newTime(ts);
		Alarm alarm = ValueFactory.alarmNone();
		String alarmStr = columns[severity];
		if (alarmStr != null) {
			for (AlarmSeverity s : AlarmSeverity.values()) {
				if (alarmStr.startsWith(s.name())) {
					alarm = ValueFactory.newAlarm(s, alarmStr);
					break;
				}
			}
		}
		Display display = ValueFactory.displayNone();
		
		// Determine the value type
		// Try double
		if (columns[float_val] != null && !columns[float_val].isEmpty()) {
			Double dbl0 = Double.valueOf(columns[float_val]);
			// Get array elements - if any.
			final double data[] = readBlobArrayElements(dbl0, columns);
			if (data.length == 1) {
				return ValueFactory.newVDouble(data[0], alarm, time, display);
			} else {
				return ValueFactory.newVDoubleArray(new ArrayDouble(data),
						alarm, time, display);
			}
		}

		// Try integer
		if (columns[num_val] != null && !columns[num_val].isEmpty()) {
			final int num = Integer.valueOf(columns[num_val]);
			return ValueFactory.newVInt(num, alarm, time, display);
		}

		// Default to string
		final String txt = columns[str_val];
		return ValueFactory.newVString(txt, alarm, time);
	}

	private double[] readBlobArrayElements(final double dbl0, final String[] columns)
			throws Exception {
		final String datatype = columns[array_nval];

		// ' ' or NULL indicate: Scalar, not an array
		if (datatype == null || datatype.isEmpty())
			return new double[] { dbl0 };

		// Decode BLOB
		final String[] values = columns[array_val].split(" ");
		final double[] array = new double[values.length];
		for (int i = 0; i < values.length; i++)
			array[i] = Double.valueOf(values[i]);

		return array;
	}

	private Timestamp fromSQLTimestamp(final java.sql.Timestamp sql_time) {
		final long millisecs = sql_time.getTime();
		final long seconds = millisecs / 1000;
		final int nanoseconds = sql_time.getNanos();
		return Timestamp.of(seconds, nanoseconds);
	}

	private String[] getColumns(final String line) {
		return line.split(csvSplitBy, -1);
	}

}
