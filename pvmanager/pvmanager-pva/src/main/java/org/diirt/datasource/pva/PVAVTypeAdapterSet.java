/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pva;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.StandardFieldFactory;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.diirt.datasource.pva.adapters.PVANTNDArray;
import org.diirt.datasource.pva.adapters.PVFieldNTHistogramToVIntArray;
import org.diirt.datasource.pva.adapters.PVFieldNTHistogramToVLongArray;
import org.diirt.datasource.pva.adapters.PVFieldNTHistogramToVShortArray;
import org.diirt.datasource.pva.adapters.PVFieldNTMatrixToVDoubleArray;
import org.diirt.datasource.pva.adapters.PVFieldNTNameValueToVTable;
import org.diirt.datasource.pva.adapters.PVFieldToVBoolean;
import org.diirt.datasource.pva.adapters.PVFieldToVByte;
import org.diirt.datasource.pva.adapters.PVFieldToVByteArray;
import org.diirt.datasource.pva.adapters.PVFieldToVDouble;
import org.diirt.datasource.pva.adapters.PVFieldToVDoubleArray;
import org.diirt.datasource.pva.adapters.PVFieldToVEnum;
import org.diirt.datasource.pva.adapters.PVFieldToVFloat;
import org.diirt.datasource.pva.adapters.PVFieldToVFloatArray;
import org.diirt.datasource.pva.adapters.PVFieldToVInt;
import org.diirt.datasource.pva.adapters.PVFieldToVIntArray;
import org.diirt.datasource.pva.adapters.PVFieldToVLong;
import org.diirt.datasource.pva.adapters.PVFieldToVLongArray;
import org.diirt.datasource.pva.adapters.PVFieldToVShort;
import org.diirt.datasource.pva.adapters.PVFieldToVShortArray;
import org.diirt.datasource.pva.adapters.PVFieldToVStatistics;
import org.diirt.datasource.pva.adapters.PVFieldToVString;
import org.diirt.datasource.pva.adapters.PVFieldToVStringArray;
import org.diirt.datasource.pva.adapters.PVFieldToVTable;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VByte;
import org.diirt.vtype.VByteArray;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VFloat;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VIntArray;
import org.diirt.vtype.VLong;
import org.diirt.vtype.VLongArray;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VShort;
import org.diirt.vtype.VShortArray;
import org.diirt.vtype.VStatistics;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTable;

/**
 *
 * @author msekoranja
 */
public class PVAVTypeAdapterSet implements PVATypeAdapterSet {
    
	private static final FieldCreate fieldCreate = FieldFactory.getFieldCreate();
    @Override
    public Set<PVATypeAdapter> getAdapters() {
        return converters;
    }
    
    //  -> VDouble
    final static PVATypeAdapter ToVDouble = new PVATypeAdapter(
    		VDouble.class, 
    		new String[] { "ev4:nt/NTScalar:1.0", "scalar_t" }, 
    		new Field[]
    				{
    					fieldCreate.createScalar(ScalarType.pvDouble)
    				})
    	{
            @Override
            public VDouble createValue(PVStructure message, Field valueType, boolean disconnected) {
                return new PVFieldToVDouble(message, disconnected);
            }
        };

    //  -> VFloat
    final static PVATypeAdapter ToVFloat = new PVATypeAdapter(
    		VFloat.class, 
    		new String[] { "ev4:nt/NTScalar:1.0", "scalar_t" }, 
    		new Field[]
    				{
    					fieldCreate.createScalar(ScalarType.pvFloat)
    				})
    	{
            @Override
            public VFloat createValue(PVStructure message, Field valueType, boolean disconnected) {
                return new PVFieldToVFloat(message, disconnected);
            }
        };
        
    //  -> VByte
    final static PVATypeAdapter ToVByte = new PVATypeAdapter(
    		VByte.class,
    		new String[] { "ev4:nt/NTScalar:1.0", "scalar_t" },
    		new Field[]
    				{
    					fieldCreate.createScalar(ScalarType.pvByte),
    					fieldCreate.createScalar(ScalarType.pvUByte)
    				})
    	{

            @Override
            public VByte createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVByte(message, disconnected);
            }
        };

    //  -> VShort
    final static PVATypeAdapter ToVShort = new PVATypeAdapter(
    		VShort.class,
    		new String[] { "ev4:nt/NTScalar:1.0", "scalar_t" },
    		new Field[]
    				{
    					fieldCreate.createScalar(ScalarType.pvShort),
    					fieldCreate.createScalar(ScalarType.pvUShort)
    				})
    	{

            @Override
            public VShort createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVShort(message, disconnected);
            }
        };

    //  -> VInt
    final static PVATypeAdapter ToVInt = new PVATypeAdapter(
    		VInt.class,
    		new String[] { "ev4:nt/NTScalar:1.0", "scalar_t" },
    		new Field[]
    				{
    					fieldCreate.createScalar(ScalarType.pvInt),
    					fieldCreate.createScalar(ScalarType.pvUInt)
    				})
    	{

            @Override
            public VInt createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVInt(message, disconnected);
            }
        };
        
    //  -> VLong
    final static PVATypeAdapter ToVLong = new PVATypeAdapter(
    		VLong.class,
    		new String[] { "ev4:nt/NTScalar:1.0", "scalar_t" },
    		new Field[]
    				{
    					fieldCreate.createScalar(ScalarType.pvLong),
    					fieldCreate.createScalar(ScalarType.pvULong),
    				})
    	{

            @Override
            public VLong createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVLong(message, disconnected);
            }
        };
        
    //  -> VBoolean
    final static PVATypeAdapter ToVBoolean = new PVATypeAdapter(
    		VBoolean.class,
    		new String[] { "ev4:nt/NTScalar:1.0", "scalar_t" },
    		new Field[]
    				{
    					fieldCreate.createScalar(ScalarType.pvBoolean)
    				})
    	{

            @Override
            public VBoolean createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVBoolean(message, disconnected);
            }
        };
        
    //  -> VString
    final static PVATypeAdapter ToVString = new PVATypeAdapter(
    		VString.class,
    		new String[] { "ev4:nt/NTScalar:1.0", "scalar_t" },
			fieldCreate.createScalar(ScalarType.pvString))
    	{
            @Override
            public VString createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVString(message, disconnected);
            }
        };
            
    //  -> VEnum
    final static PVATypeAdapter ToVEnum = new PVATypeAdapter(
    		VEnum.class,
    		new String[] { "ev4:nt/NTEnum:1.0", "enum_t" },
    		StandardFieldFactory.getStandardField().enumerated())
    	{
            @Override
            public VEnum createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVEnum(message, disconnected);
            }
        };

    //  -> VArrayDouble
    final static PVATypeAdapter ToVArrayDouble = new PVATypeAdapter(
    		VDoubleArray.class,
    		new String[] { "ev4:nt/NTScalarArray:1.0", "scalar_t[]" },
    		fieldCreate.createScalarArray(ScalarType.pvDouble))
    	{
            @Override
            public VDoubleArray createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVDoubleArray(message, disconnected);
            }
        };

    //  -> VArrayFloat
    final static PVATypeAdapter ToVArrayFloat = new PVATypeAdapter(
    		VFloatArray.class,
    		new String[] { "ev4:nt/NTScalarArray:1.0", "scalar_t[]" },
    		fieldCreate.createScalarArray(ScalarType.pvFloat))
    	{
            @Override
            public VFloatArray createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVFloatArray(message, disconnected);
            }
        };
        
    //  -> VArrayInt
    final static PVATypeAdapter ToVArrayInt = new PVATypeAdapter(
    		VIntArray.class,
    		new String[] { "ev4:nt/NTScalarArray:1.0", "scalar_t[]" },
    		fieldCreate.createScalarArray(ScalarType.pvInt))
    	{
            @Override
            public VIntArray createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVIntArray(message, disconnected);
            }
        };

    //  -> VArrayLong
    final static PVATypeAdapter ToVArrayLong = new PVATypeAdapter(
    		VLongArray.class,
    		new String[] { "ev4:nt/NTScalarArray:1.0", "scalar_t[]" },
    		fieldCreate.createScalarArray(ScalarType.pvLong))
    	{
            @Override
            public VLongArray createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVLongArray(message, disconnected);
            }
        };
        
    //  -> VArrayShort
    final static PVATypeAdapter ToVArrayShort = new PVATypeAdapter(
    		VShortArray.class,
    		new String[] { "ev4:nt/NTScalarArray:1.0", "scalar_t[]" },
    		fieldCreate.createScalarArray(ScalarType.pvShort))
    	{
            @Override
            public VShortArray createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVShortArray(message, disconnected);
            }
        };
        
    //  -> VArrayByte
    final static PVATypeAdapter ToVArrayByte = new PVATypeAdapter(
    		VByteArray.class,
    		new String[] { "ev4:nt/NTScalarArray:1.0", "scalar_t[]" },
    		fieldCreate.createScalarArray(ScalarType.pvByte))
    	{
            @Override
            public VByteArray createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVByteArray(message, disconnected);
            }
        };
        
    //  -> VArrayString
    final static PVATypeAdapter ToVArrayString = new PVATypeAdapter(
    		VStringArray.class,
    		new String[] { "ev4:nt/NTScalarArray:1.0", "scalar_t[]" },
    		fieldCreate.createScalarArray(ScalarType.pvString))
    	{
            @Override
            public VStringArray createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVStringArray(message, disconnected);
            }
        };
        
    //  -> VTable
    final static PVATypeAdapter ToVTable = new PVATypeAdapter(
    		VTable.class,
    		new String[] { "ev4:nt/NTTable:1.0" })
    	{
            @Override
            public VTable createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVTable(message, disconnected);
            }
        };

    //  -> VDoubleArray as matrix (NTMatrix support)
    final static PVATypeAdapter ToVDoubleArrayAsMatrix = new PVATypeAdapter(
    		VDoubleArray.class,
    		new String[] { "ev4:nt/NTMatrix:1.0" })
    	{
            @Override
            public VDoubleArray createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldNTMatrixToVDoubleArray(message, disconnected);
            }
        };
        
    //  -> VTable as name-value (NTNameValue support)
    final static PVATypeAdapter ToVTableAsNameValue = new PVATypeAdapter(
    		VTable.class,
    		new String[] { "ev4:nt/NTNameValue:1.0" })
    	{
            @Override
            public VTable createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldNTNameValueToVTable(message, disconnected);
            }
        };

    //  -> VStatistics 
    final static PVATypeAdapter ToVStatistics = new PVATypeAdapter(
    		VStatistics.class,
    		new String[] { "ev4:nt/NTAggregate:1.0" })
    	{
            @Override
            public VStatistics createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVStatistics(message, disconnected);
            }
        };

    //  -> VNumberArray (NTHistogram support) 
    final static PVATypeAdapter ToVNumberArrayAsHistogram = new PVATypeAdapter(
    		VNumberArray.class,
    		new String[] { "ev4:nt/NTHistogram:1.0" })
    	{
            @Override
            public VNumberArray createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	PVField valueField = message.getSubField("value");
            	if (valueField instanceof PVScalarArray)
            	{
            		switch (((PVScalarArray)valueField).getScalarArray().getElementType())
            		{
            			case pvShort: return new PVFieldNTHistogramToVShortArray(message, disconnected);
            			case pvInt  : return new PVFieldNTHistogramToVIntArray(message, disconnected);
            			case pvLong : return new PVFieldNTHistogramToVLongArray(message, disconnected);
            			default:
                    		throw new RuntimeException("NTHistogram 'value' scalar array must be { short[] | int[] | long[] }.");
            		}
            		
            	}
            	else
            		throw new RuntimeException("NTHistogram does not have a scalar array 'value' field.");
            }
        };

    //  -> VStatistics 
    final static PVATypeAdapter ToNTNDArray = new PVATypeAdapter(
    		PVANTNDArray.class,
    		new String[] { "ev4:nt/NTNDArray:1.0" })
    	{
            @Override
            public PVANTNDArray createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVANTNDArray(message, disconnected);
            }
        };
            
    private static final Set<PVATypeAdapter> converters;
    
    static {
        Set<PVATypeAdapter> newFactories = new HashSet<PVATypeAdapter>();
        
        // Add all SCALARs
        newFactories.add(ToVDouble);
        newFactories.add(ToVFloat);
        newFactories.add(ToVByte);
        newFactories.add(ToVShort);
        newFactories.add(ToVInt);
        newFactories.add(ToVLong);
        newFactories.add(ToVString);
        newFactories.add(ToVEnum);
        newFactories.add(ToVBoolean);

        // Add all ARRAYs
        newFactories.add(ToVArrayDouble);
        newFactories.add(ToVArrayFloat);
        newFactories.add(ToVArrayByte);
        newFactories.add(ToVArrayShort);
        newFactories.add(ToVArrayInt);
        newFactories.add(ToVArrayLong);
        newFactories.add(ToVArrayString);
        //newFactories.add(ToVArrayEnum);
        // no VBooleanArray
        
        newFactories.add(ToVTable);
        newFactories.add(ToVDoubleArrayAsMatrix);	// NTMatrix support
        newFactories.add(ToVTableAsNameValue);	// NTNameValue support

        newFactories.add(ToVStatistics); // NTAggregate support
        newFactories.add(ToVNumberArrayAsHistogram); // NTHistogram support

        newFactories.add(ToNTNDArray); // NTNDArray support

        converters = Collections.unmodifiableSet(newFactories);
    }
    
    // TODO
    /*
VEnumArray - not explicitly a NT
VMultiDouble - missing Display
VMultiEnum  - missing Display
VMultiInt - missing Display
VMultiString  - missing Display
    */
}
