/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a type of PV.
 *
 * @author carcassi
 */
public abstract class PVType<T extends PVType<T>> {

    // It handles the PVValueChangeListener support, so that each
    // PV type does not have to implement it.

    private List<PVValueChangeListener> valueChangeListeners = new ArrayList<PVValueChangeListener>();

    protected void firePvValueChanged() {
        for (PVValueChangeListener listener : valueChangeListeners) {
            listener.pvValueChanged();
        }
    }

    protected void addPVValueChangeListener(PVValueChangeListener listener) {
        valueChangeListeners.add(listener);
    }

    protected void removePVValueChangeListener(PVValueChangeListener listener) {
        valueChangeListeners.remove(listener);
    }

    /**
     * Changes the value of this instance to the one given, required to have
     * the data change "in place". This method must
     * use the lock on newValue before accessing the data.
     *
     * @param newValue the new value
     */
    abstract void setTo(T newValue);

}
