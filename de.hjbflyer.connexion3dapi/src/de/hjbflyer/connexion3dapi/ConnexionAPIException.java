package de.hjbflyer.connexion3dapi;

/**
 * Handle exceptions in the ConnexionAPI.
 * 
 * @author hjbflyer
 * 
 * Copyright(c) hjbflyer 2017
 *
 */
public class ConnexionAPIException extends Exception {

    /** serialVersionUID long */
    private static final long serialVersionUID = -5338670054453167868L;

    /**
     * @param string message
     */
    public ConnexionAPIException(String string) {
        super(string);
    }
}
