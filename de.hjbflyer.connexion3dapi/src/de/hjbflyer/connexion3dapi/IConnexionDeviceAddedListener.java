/**
 * de.hjbrede.jni
 * IConnexionDeviceAddedListener.java
 * copyright(c) hjb 05.03.2017
 */
package de.hjbflyer.connexion3dapi;

/**
 * IConnexionDeviceAddedListener is an interface for listeners which are interested if a device has been added.
 * 
 * @author hjbflyer
 * 
 * Copyright(c) hjbflyer 2017
 *
 */
public interface IConnexionDeviceAddedListener {

    /**
     * @param id device id
     */
    public void deviceAdded(int id);

}
