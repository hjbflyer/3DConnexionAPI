/**
 * de.hjbrede.jni
 * IConnexionPrefsChangedListener.java
 * copyright(c) hjb 05.03.2017
 */
package de.hjbflyer.connexion3dapi;

/**
 * IConnexionPrefsChangedListeneris an interface for listeners which are interested in device prefs changed events.
 * 
 * @author hjbflyer
 * 
 * Copyright(c) hjbflyer 2017
 *
 */
public interface IConnexionPrefsChangedListener {

    
    /**
     * @param prefs ConnexionDevicePrefs
     */
    public void prefsChanged(ConnexionDevicePrefs prefs);
}
