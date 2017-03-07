/**
 * de.hjbrede.jni
 * ConnexionClientPrefs.java
 * copyright(c) hjb 04.03.2017
 */
package de.hjbflyer.connexion3dapi;

import java.util.Arrays;

/**
 * ConnexionDevicePrefs is a re-implementation of the C++ structure from the API.
 * 
 * @author hjbflyer
 * 
 * Copyright(c) hjbflyer 2017
 *
 */
public class ConnexionDevicePrefs {

    /** AXIS number of axis */
    private static final int AXIS = ConnexionAPI.AXIS;

    // header
    /** m_type kConnexionDevicePrefsType */
    private int m_type; 
    /** m_version kConnexionDevicePrefsVers */
    private int m_version;
    /** m_deviceID device ID (SpaceNavigator, SpaceNavigatorNB, SpaceExplorer...) */
    private int m_deviceID;
    /** m_reserved1 set to 0 */
    private int m_reserved1;
    // target application
    /** m_appSignature target application signature */
    private long m_appSignature;
    /** m_reserved2  set to 0 */
    private long m_reserved2;
    /** m_appName  target application name */
    private String m_appName;
    // device preferences
    /** m_mainSpeed overall speed */
    private int m_mainSpeed;
    /** m_zoomOnY use Y axis for zoom, Z axis for up/down pan */
    private int m_zoomOnY;
    /** dominant only respond to the largest one of all 6 axes values at any given time */
    private int m_dominant;
    /** m_reserved3 set to 0 */
    private int m_reserved3;
    /** m_mapV axes mapping when Zoom direction is on vertical axis (zoomOnY = 0) */
    private int[] m_mapV = new int[AXIS];
    /** m_mapH axes mapping when Zoom direction is on horizontal axis (zoomOnY != 0) */
    private int[] m_mapH = new int[AXIS]; 
    /** m_enabled enable or disable individual axes */
    private int[] m_enabled = new int[AXIS];
    /** m_reversed reverse individual axes */
    private int[] m_reversed = new int[AXIS];
    /** m_speed speed for individual axes (min 0, max 200, reserved 201-255) */
    private int[] m_speed = new int[AXIS];
    /** m_sensitivity sensitivity for individual axes (min 0, max 200, reserved 201-255) */
    private int[] m_sensitivity = new int[AXIS];
    /** m_scale 10000 * scale and "natural" reverse state for individual axes */
    private long[] m_scale = new long[AXIS];
    // added in version 10.0 (build 136)
    /** m_gamma 1000 * gamma value used to compute nonlinear axis response, use 1000 (1.0) for linear response */
    private long m_gamma;
    /** m_intersect intersect value used for gamma computations */
    private long m_intersect;

    /**
     * 
     */
    public ConnexionDevicePrefs() {
        //empty
    }

    /**
     * @return the type
     */
    public int getType() {
        return m_type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        m_type = type;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return m_version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        m_version = version;
    }

    /**
     * @return the deviceID
     */
    public int getDeviceID() {
        return m_deviceID;
    }

    /**
     * @param deviceID the deviceID to set
     */
    public void setDeviceID(int deviceID) {
        m_deviceID = deviceID;
    }

    /**
     * @return the reserved1
     */
    public int getReserved1() {
        return m_reserved1;
    }

    /**
     * @param reserved1 the reserved1 to set
     */
    public void setReserved1(int reserved1) {
        m_reserved1 = reserved1;
    }

    /**
     * @return the appSignature
     */
    public long getAppSignature() {
        return m_appSignature;
    }

    /**
     * @param appSignature the appSignature to set
     */
    public void setAppSignature(long appSignature) {
        m_appSignature = appSignature;
    }

    /**
     * @return the reserved2
     */
    public long getReserved2() {
        return m_reserved2;
    }

    /**
     * @param reserved2 the reserved2 to set
     */
    public void setReserved2(long reserved2) {
        m_reserved2 = reserved2;
    }

    /**
     * @return the appName
     */
    public String getAppName() {
        return m_appName;
    }

    /**
     * @param appName the appName to set
     */
    public void setAppName(String appName) {
        m_appName = appName;
    }

    /**
     * @return the mainSpeed
     */
    public int getMainSpeed() {
        return m_mainSpeed;
    }

    /**
     * @param mainSpeed the mainSpeed to set
     */
    public void setMainSpeed(int mainSpeed) {
        m_mainSpeed = mainSpeed;
    }

    /**
     * @return the zoomOnY
     */
    public int getZoomOnY() {
        return m_zoomOnY;
    }

    /**
     * @param zoomOnY the zoomOnY to set
     */
    public void setZoomOnY(int zoomOnY) {
        m_zoomOnY = zoomOnY;
    }

    /**
     * @return the dominant
     */
    public int getDominant() {
        return m_dominant;
    }

    /**
     * @param dominant the dominant to set
     */
    public void setDominant(int dominant) {
        m_dominant = dominant;
    }

    /**
     * @return the reserved3
     */
    public int getReserved3() {
        return m_reserved3;
    }

    /**
     * @param reserved3 the reserved3 to set
     */
    public void setReserved3(int reserved3) {
        m_reserved3 = reserved3;
    }

    /**
     * @return the mapV
     */
    public int[] getMapV() {
        return m_mapV;
    }

    /**
     * @param mapV the mapV to set
     */
    public void setMapV(int[] mapV) {
        m_mapV = mapV;
    }

    /**
     * @return the mapH
     */
    public int[] getMapH() {
        return m_mapH;
    }

    /**
     * @param mapH the mapH to set
     */
    public void setMapH(int[] mapH) {
        m_mapH = mapH;
    }

    /**
     * @return the enabled
     */
    public int[] getEnabled() {
        return m_enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(int[] enabled) {
        m_enabled = enabled;
    }

    /**
     * @return the reversed
     */
    public int[] getReversed() {
        return m_reversed;
    }

    /**
     * @param reversed the reversed to set
     */
    public void setReversed(int[] reversed) {
        m_reversed = reversed;
    }

    /**
     * @return the speed
     */
    public int[] getSpeed() {
        return m_speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(int[] speed) {
        m_speed = speed;
    }

    /**
     * @return the sensitivity
     */
    public int[] getSensitivity() {
        return m_sensitivity;
    }

    /**
     * @param sensitivity the sensitivity to set
     */
    public void setSensitivity(int[] sensitivity) {
        m_sensitivity = sensitivity;
    }

    /**
     * @return the scale
     */
    public long[] getScale() {
        return m_scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(long[] scale) {
        m_scale = scale;
    }

    /**
     * @return the gamma
     */
    public long getGamma() {
        return m_gamma;
    }

    /**
     * @param gamma the gamma to set
     */
    public void setGamma(long gamma) {
        m_gamma = gamma;
    }

    /**
     * @return the intersect
     */
    public long getIntersect() {
        return m_intersect;
    }

    /**
     * @param intersect the intersect to set
     */
    public void setIntersect(long intersect) {
        m_intersect = intersect;
    }

    /** 
     * {@InheritDoc}
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ConnexionDevicePrefs [type="); //$NON-NLS-1$
        builder.append(m_type);
        builder.append(", version="); //$NON-NLS-1$
        builder.append(m_version);
        builder.append(", deviceID="); //$NON-NLS-1$
        builder.append(m_deviceID);
        builder.append(", reserved1="); //$NON-NLS-1$
        builder.append(m_reserved1);
        builder.append(", appSignature="); //$NON-NLS-1$
        builder.append(m_appSignature);
        builder.append(", reserved2="); //$NON-NLS-1$
        builder.append(m_reserved2);
        builder.append(", appName="); //$NON-NLS-1$
        builder.append(m_appName);
        builder.append(", mainSpeed="); //$NON-NLS-1$
        builder.append(m_mainSpeed);
        builder.append(", zoomOnY="); //$NON-NLS-1$
        builder.append(m_zoomOnY);
        builder.append(", dominant="); //$NON-NLS-1$
        builder.append(m_dominant);
        builder.append(", reserved3="); //$NON-NLS-1$
        builder.append(m_reserved3);
        builder.append(", mapV="); //$NON-NLS-1$
        builder.append(Arrays.toString(m_mapV));
        builder.append(", mapH="); //$NON-NLS-1$
        builder.append(Arrays.toString(m_mapH));
        builder.append(", enabled="); //$NON-NLS-1$
        builder.append(Arrays.toString(m_enabled));
        builder.append(", reversed="); //$NON-NLS-1$
        builder.append(Arrays.toString(m_reversed));
        builder.append(", speed="); //$NON-NLS-1$
        builder.append(Arrays.toString(m_speed));
        builder.append(", sensitivity="); //$NON-NLS-1$
        builder.append(Arrays.toString(m_sensitivity));
        builder.append(", scale="); //$NON-NLS-1$
        builder.append(Arrays.toString(m_scale));
        builder.append(", gamma="); //$NON-NLS-1$
        builder.append(m_gamma);
        builder.append(", intersect="); //$NON-NLS-1$
        builder.append(m_intersect);
        builder.append("]"); //$NON-NLS-1$
        return builder.toString();
    }

}
