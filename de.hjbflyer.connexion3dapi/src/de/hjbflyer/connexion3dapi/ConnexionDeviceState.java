/**
 * de.hjbrede.jni
 * ConnexionDeviceState.java
 * copyright(c) hjb 04.03.2017
 */
package de.hjbflyer.connexion3dapi;

/**
 * ConnexionDeviceState is a re-implementation of the C++ structure from the API.
 * 
 * @author hjbflyer
 * 
 * Copyright(c) hjbflyer 2017
 *
 */
public class ConnexionDeviceState {
    // header
    /** m_version kConnexionDeviceStateVers */
    private int m_version;
    /** m_client identifier of the target client when sending a state message to all user clients */
    private int m_client;
    // command
    /** m_command command for the user-space client */
    private int m_command;
    /** m_param optional parameter for the specified command */
    private int m_param;
    /** m_value optional value for the specified command */
    private long m_value;
    /** m_time time stamp for this message (clock_get_uptime) */
    private long m_time;
    // raw report
    /** m_report raw USB report from the device processed data */
    private char[] m_report = new char[8];
    /** m_buttons8 buttons (first 8 buttons only, for backwards binary compatibility- use "buttons" field instead) */
    private int m_buttons8; 
    /** m_axis  x, y, z, rx, ry, rz */
    /** m_axis int[] */
    private int[] m_axis = new int[ConnexionAPI.AXIS]; // x, y, z, rx, ry, rz
    /** m_address USB device address, used to tell one device from the other */
    private int m_address; // 
    /** buttons buttons */
    private long m_buttons;

    /**
     * 
     */
    public ConnexionDeviceState() {
        // empty
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
     * @return the client
     */
    public int getClient() {
        return m_client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(int client) {
        m_client = client;
    }

    /**
     * @return the command
     */
    public int getCommand() {
        return m_command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(int command) {
        m_command = command;
    }

    /**
     * @return the param
     */
    public int getParam() {
        return m_param;
    }

    /**
     * @param param the param to set
     */
    public void setParam(int param) {
        m_param = param;
    }

    /**
     * @return the value
     */
    public long getValue() {
        return m_value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(long value) {
        m_value = value;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return m_time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(long time) {
        m_time = time;
    }

    /**
     * @return the report
     */
    public char[] getReport() {
        return m_report;
    }

    /**
     * @param report the report to set
     */
    public void setReport(char[] report) {
        m_report = report;
    }

    /**
     * @return the buttons8
     */
    public int getButtons8() {
        return m_buttons8;
    }

    /**
     * @param buttons8 the buttons8 to set
     */
    public void setButtons8(int buttons8) {
        m_buttons8 = buttons8;
    }

    /**
     * @return the axis
     */
    public int[] getAxis() {
        return m_axis;
    }

    /**
     * @param axis the axis to set
     */
    public void setAxis(int[] axis) {
        m_axis = axis;
    }

    /**
     * @return the address
     */
    public int getAddress() {
        return m_address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(int address) {
        m_address = address;
    }

    /**
     * @return the buttons
     */
    public long getButtons() {
        return m_buttons;
    }

    /**
     * @param buttons the buttons to set
     */
    public void setButtons(long buttons) {
        m_buttons = buttons;
    }

}
