/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pva.adapters;

import org.epics.pvdata.pv.PVBoolean;
import org.epics.pvdata.pv.PVStructure;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VTypeToString;

/**
 * @author dkumar
 */
public class PVFieldToVBoolean extends AlarmTimeDisplayExtractor implements VBoolean {
 
  protected final Boolean value;


  /**
   * @param pvField
   * @param disconnected
   */
  public PVFieldToVBoolean(PVStructure pvField, boolean disconnected) {
    super(pvField, disconnected);

    PVBoolean booleanField = pvField.getBooleanField("value");
    if (booleanField != null) {
      value = booleanField.get();
    } else {
      value = null;
    }
  }


  @Override
  public Boolean getValue() {
    return value;
  }


  @Override
  public String toString() {
    return VTypeToString.toString(this);
  }
}
