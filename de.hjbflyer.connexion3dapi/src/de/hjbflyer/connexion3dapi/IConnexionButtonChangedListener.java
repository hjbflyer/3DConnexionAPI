/**
 * de.hjbrede.jni
 * IConnexionButtonChangedListener.java
 * copyright(c) hjb 04.03.2017
 */
package de.hjbflyer.connexion3dapi;

/**
 * IConnexionButtonChangedListener is an interface for listeners which are interested in button events.
 * 
 * @author hjbflyer
 * 
 * Copyright(c) hjbflyer 2017
 *
 */
public interface IConnexionButtonChangedListener {
    
    /**
     * @param button button
     */
    public void buttonChanged(long button);

}
