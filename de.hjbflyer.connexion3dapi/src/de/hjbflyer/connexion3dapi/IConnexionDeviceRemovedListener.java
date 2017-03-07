/**
 * de.hjbrede.jni
 * IConnexionDeviceRemovedListener.java
 * copyright(c) hjb 05.03.2017
 */
package de.hjbflyer.connexion3dapi;

/**
 * IConnexionDeviceRemovedListener is an interface for listeners which are interested if a device has been removed.
 * 
 * @author hjbflyer
 * 
 * Copyright(c) hjbflyer 2017
 *
 *
 */
public interface IConnexionDeviceRemovedListener {


    /**
     * @param id device id
     */
    public void deviceRemoved(int id);
}
