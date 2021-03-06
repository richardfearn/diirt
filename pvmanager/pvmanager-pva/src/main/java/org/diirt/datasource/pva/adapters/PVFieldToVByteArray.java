/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pva.adapters;


import java.util.List;
import org.epics.pvdata.pv.ByteArrayData;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.diirt.vtype.VByteArray;
import org.diirt.vtype.VTypeToString;
import org.diirt.util.array.ArrayByte;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ListByte;
import org.diirt.util.array.ListInt;
import org.diirt.vtype.ArrayDimensionDisplay;
import org.diirt.vtype.ValueUtil;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVByteArray extends AlarmTimeDisplayExtractor implements VByteArray {

	private final ListInt size;
	private final ListByte list;
	
	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVByteArray(PVStructure pvField, String fieldName, boolean disconnected) {
		super(pvField, disconnected);
		
		PVByteArray valueField =
			(PVByteArray)pvField.getScalarArrayField(fieldName, ScalarType.pvByte);
		if (valueField != null)
		{
			ByteArrayData data = new ByteArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.size = new ArrayInt(data.data.length);
			this.list = new ArrayByte(data.data);
		}
		else
		{
			size = null;
			list = null;
		}
	}

	public PVFieldToVByteArray(PVStructure pvField, boolean disconnected) {
		this(pvField, "value", disconnected);
	}
	
	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.Array#getSizes()
	 */
	@Override
	public ListInt getSizes() {
		return size;
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VByteArray#getData()
	 */
	@Override
	public ListByte getData() {
		return list;
	}
    
    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

    @Override
    public List<ArrayDimensionDisplay> getDimensionDisplay() {
        return ValueUtil.defaultArrayDisplay(this);
    }

}
