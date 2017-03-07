/**
 * de.hjbrede.jni
 * IConnexionAxisChanged.java
 * copyright(c) hjb 04.03.2017
 */
package de.hjbflyer.connexion3dapi;

/**
 * IConnexionAxisChangedListener is an interface for listeners which are interested in axis changes events.
 * 
 * @author hjbflyer
 * 
 * Copyright(c) hjbflyer 2017
 *
 */
public interface IConnexionAxisChangedListener {
    
    /**
     * @param values of the axis
     */
    public void axisChanged(int[] values);

}
