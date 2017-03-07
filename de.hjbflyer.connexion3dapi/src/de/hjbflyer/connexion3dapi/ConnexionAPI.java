package de.hjbflyer.connexion3dapi;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a Java implementation of the 3DconnexionAPI. The comments are taken from the SDK
 * documentation and from the framework header files.
 * <p>
 * 
 * The API is not implemented in a way that all API calls a transfered to Java. The idea is to
 * provide a simple to use API. Details might be hidden in the C++ layer.
 * <p>
 * The Java interface is simple.
 * <ol>
 * <li>Instantiate the API with (String appName) the name of the Application as argument.
 * <li>Depending on the events your application is interested in, you should implement one of the
 * following listeners:
 * <ul>
 * <li>The {@link IConnexionAxisChangedListener} is called on changes on the different axis. Each
 * event gives an array of six integer values for the six axis.
 * <li>{@link IConnexionButtonChangedListener} returns an integer with as a bit mask. The bits
 * represent the different buttons.
 * <li>{@link IConnexionDeviceAddedListener} is called when a 3DConnexion device is plugged into the
 * system,
 * <li>{@link IConnexionDeviceRemovedListener} is called when a 3DConnexion device is removed from
 * the system.
 * <li>{@link IConnexionPrefsChangedListener} is called when the device preferences have been
 * changed.
 * </ul>
 * <li>Finally call {@link #closeAndCleanUp() closeAndCleanUp}.
 * </ol>
 * 
 * @author hjbflyer
 * 
 * Copyright(c) hjbflyer 2017
 *
 */
public class ConnexionAPI {

    static {
        System.loadLibrary("ConnexionAPI"); //$NON-NLS-1$
    }
    // CHECKSTYLE:OFF
    /* constant values from ConnexionClient.h */
    public static final int AXIS = 6;
    public static final int CTRL_SET_LED_STATE     = 0x3364736c;
    public static final int CTRL_GET_CLIENT_ID     = 0x33646964;
    public static final int CTRL_CALIBRATE         = 0x33646361;
    public static final int CTRL_DECALIBRATE       = 0x33646465;
    public static final int CTRL_OPEN_PREFS        = 0x33646f70;
    public static final int CTRL_SET_SWITCHES      = 0x33647373;
    public static final int CTRL_ACTIVATE_CLIENT   = 0x33646163;
    public static final int CTRL_DEACTIVATE_CLIENT = 0x33646463;
    
    private static final int CLIENT_WILDCARD = 0x2A2A2A2A;
    private static final int CLIENT_MANUAL = 0x2B2B2B2B;
    private static final boolean USE_SEPARATE_THREAD = true;
    private static final int CLIENT_MODE_TAKE_OVER = 1;
    private static final int MASK_ALL = 0x3FFF;
    // private static int s = '3' << 24 + 'd' << 16 + 's' << 8 + 'l';

    // CHECKSTYLE:ON
    /** m_axisListeners List<IConnexionAxisChangedListener> */
    private List<IConnexionAxisChangedListener> m_axisListeners = new ArrayList<>();
    /** m_buttonListeners List<IConnexionButtonChangedListener> */
    private List<IConnexionButtonChangedListener> m_buttonListeners = new ArrayList<>();
    /** m_deviceAddedListeners List<IConnexionDeviceAddedListener> */
    private List<IConnexionDeviceAddedListener> m_deviceAddedListeners = new ArrayList<>();
    /** m_deviceRemovedListeners List<IConnexionDeviceRemovedListener> */
    private List<IConnexionDeviceRemovedListener> m_deviceRemovedListeners = new ArrayList<>();
    /** m_prefsChangedListeners List<IConnexionPrefsChangedListener> */
    private List<IConnexionPrefsChangedListener> m_prefsChangedListeners = new ArrayList<>();

    /**
     * The constructor sets up the message handling and registers the application with the device handler. 
     * @param execName name of the application
     */
    public ConnexionAPI(String execName) {
        try {
            setConnexionHandlers();
            registerConnexion(execName);
        } catch (ConnexionAPIException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnect the application from the device and clean up handlers.
     */
    public void closeAndCleanUp() {
        unregisterConnexionClient();
        cleanUpConnexionHandler();
        m_axisListeners.clear();
        m_buttonListeners.clear();
        m_deviceAddedListeners.clear();
        m_deviceRemovedListeners.clear();
        m_prefsChangedListeners.clear();
    }

    
    /**
     * @param listener to add
     */
    public void addAxisChangedListener(IConnexionAxisChangedListener listener) {
        m_axisListeners.add(listener);
    }

    /**
     * @param listener to remove
     */
    public void removeAxisChangedListener(IConnexionAxisChangedListener listener) {
        m_axisListeners.remove(listener);
    }

    /**
     * @param listener to add
     */
    public void addButtonChangedListener(IConnexionButtonChangedListener listener) {
        m_buttonListeners.add(listener);
    }

    /**
     * @param listener to remove
     */
    public void removeButtonChangedListener(IConnexionButtonChangedListener listener) {
        m_buttonListeners.remove(listener);
    }

    /**
     * @param listener to add
     */
    public void addDeviceAddedListeners(IConnexionDeviceAddedListener listener) {
        m_deviceAddedListeners.add(listener);
    }

    /**
     * @param listener to remove
     */
    public void removeDeviceAddedListener(IConnexionDeviceAddedListener listener) {
        m_deviceAddedListeners.remove(listener);
    }

    /**
     * @param listener to add
     */
    public void addDeviceRemovedListeners(IConnexionDeviceRemovedListener listener) {
        m_deviceRemovedListeners.add(listener);
    }

    /**
     * @param listener to remove
     */
    public void removeDeviceRemovedListener(IConnexionDeviceRemovedListener listener) {
        m_deviceRemovedListeners.remove(listener);
    }

    /**
     * @param listener to add
     */
    public void addPrefsChangedListeners(IConnexionPrefsChangedListener listener) {
        m_prefsChangedListeners.add(listener);
    }

    /**
     * @param listener to remove
     */
    public void removePrefsChangedListener(IConnexionPrefsChangedListener listener) {
        m_prefsChangedListeners.remove(listener);
    }
    
    /* ****************************************************************************** */
    /* ****************************************************************************** */
    /* ****************************************************************************** */

    /**
     * Public APIs to be called whenever the app wants to start/stop receiving data the mask
     * parameter (client capabilities mask) specifies which controls must be forwarded to the
     * client. buttonMask (previously part of the client capabilities mask) specifies which buttons
     * must be forwarded to the client
     * 
     * @param mask mask
     */
    public native void setConnexionClientMask(long mask);

    /**
     * @param buttonMask button mask
     */
    public native void setConnexionClientButtonMask(long buttonMask);

    /**
     * connexionControl is deprecated. Use <b>connexionClientControl</b>
     * 
     * @param message message
     * @param param param
     * @param result result
     * @return error code
     */
    @Deprecated
    private native int connexionControl(int message, int param, int result);

    /**
     * connexionClientControl
     * 
     * Public API to send control commands to the driver and retrieve a result value Note: the new
     * ConnexionClientControl variant is strictly required for kConnexionCtlSetSwitches and
     * kConnexionCtlClearSwitches but also works for all other Control calls. The old variant
     * remains for backwards compatibility.
     * 
     * @param message message
     * @param param param
     * @param result result
     * @return error code
     */
    public native int connexionClientControl(int message, int param, Integer result);

    /**
     * @param prefs prefs
     * @return status
     */
    public native int connexionGetCurrentDevicePrefs(ConnexionDevicePrefs prefs);

    /**
     * connexionSetButtonLabels
     * 
     * @param labels labels
     * @param size number of labels
     * @return
     * 
     *         Labels data is a series of 32 variable-length null-terminated UTF8-encoded strings.
     *         The sequence of strings follows the SpacePilot Pro button numbering. Empty strings
     *         revert the button label to its default value. As an example, this data would set the
     *         label for button Top to "Top" and revert all other button labels to their default
     *         values:
     *         <p>
     *         0x00, // empty string for Menu<br> 0x00,// empty string for Fit<br>
     *         0x54, 0x6F, 0x70, 0x00, // utf-8 encoded "Top" string for Top<br>
     *         0x00, // empty string for Left <br>
     *         0x00, 0x00, 0x00, 0x00, // empty strings for Right, Front, etc... <br>
     *         0x00, 0x00, 0x00, 0x00, 0x00,<br>
     *         0x00, 0x00, 0x00, 0x00, 0x00,<br>
     *         0x00, 0x00, 0x00, 0x00, 0x00,<br>
     *         0x00, 0x00, 0x00, 0x00, 0x00,<br>
     *         0x00, 0x00, 0x00, 0x00
     */
    public native int connexionSetButtonLabels(String[] labels, int size);

    /* ****************************************************************************** */
    /* ****************************************************************************** */
    /* ****************************************************************************** */

    /**
     * setConnexionHandlers calls the first native method and sets the different handlers.
     * 
     * @throws ConnexionAPIException if an error occurs
     */
    private void setConnexionHandlers() throws ConnexionAPIException {

        int error = setConnexionHandlers(USE_SEPARATE_THREAD);
        if (error != 0) {
            throw new ConnexionAPIException("setConnexionHandlers failed with error: " + error); //$NON-NLS-1$
        }

    }

    /**
     * Register the application with the device.
     * 
     * @param string name of the application
     */
    private void registerConnexion(String string) {
        registerConnexionClient(CLIENT_WILDCARD, string, CLIENT_MODE_TAKE_OVER, MASK_ALL); // $NON-NLS-1$
    }

    /**
     * <b>setConnexionHandlers</b> <br>
     * The framework uses a callback mechanism to send your application button or axis events from
     * the device. In order to receive these events, you must first register your callback function
     * using SetConnexionHandlers(). The callback handlers are implemented in C++. The handlers will
     * asynchronously call the private callback procedures.
     * 
     * @param useSeparateThread is a boolean parameter. Supply true if you want to have msgHandler
     *            process the 3D Mouse events on a separate thread, otherwise false. <br>
     *            <b>Note:</b> kUseSeparateThread is new with 3DxWareMac 10.2.2. Having the 3D Mouse
     *            events processed on a separate thread will eliminate motion events being
     *            “buffered”, which could have happened if the application had some serious big work
     *            to be done on the very same thread. This is the case if the developer did not want
     *            to or had no possibility separate this work into different threads.
     *            <b>IMPORTANT:</b> If you choose to have the events processed on a separate thread,
     *            make sure that you take care about thread safety when exchanging data between the
     *            callbacks and your main thread. Also if you issue calls from within the callback
     *            into your main thread / other threads!
     * @return 0 in case of no error or os error code.
     * 
     */
    private native int setConnexionHandlers(boolean useSeparateThread);

    /**
     * Clean up handlers.
     */
    private native void cleanUpConnexionHandler();

    /**
     * <b>registerConnexionClient</b>. This call is needed to register your application with the
     * driver to start flow of events.
     * 
     * @param signature Is the application’s CFBundleSignature code.
     * @param name Application’s executable name.
     * @param mode Reserved. Must be this constant ConnexionClientModeTakeOver.
     * @param mask mask
     */
    private native void registerConnexionClient(int signature, String name, int mode, long mask);

    /**
     */
    private native void unregisterConnexionClient();

    /* ****************************************************************************** */
    /* ****************************************************************************** */
    /* ****************************************************************************** */
    /**
     * messageHandlerAxisCallback calls the registered listeners.
     * 
     * @param id id
     * @param type callback type
     * @param message 6 axis values transferred to listeners
     */
    private void messageHandlerAxisCallback(int id, int type, int[] message) {
        m_axisListeners.stream().forEach(l -> l.axisChanged(message));
    }

    /**
     * messageHandlerButtonCallback  calls the registered listeners.
     * 
     * @param id id
     * @param type callback type
     * @param buttons bit mask with status of buttons. Bit == 1 -> button pressed
     */
    private void messageHandlerButtonCallback(int id, int type, long buttons) {
        m_buttonListeners.stream().forEach(l -> l.buttonChanged(buttons));
    }

    /**
     * messageHandlerPrefsCallback calls the registered listeners.
     * @param id id
     * @param type type
     * @param prefs ConnexionDevicePrefs
     */
    private void messageHandlerPrefsCallback(int id, int type, ConnexionDevicePrefs prefs) {
        m_prefsChangedListeners.stream().forEach(l -> l.prefsChanged(prefs));
    }

    /**
     *  deviceAddedHandler calls the registered listeners.
     * @param productID id
     */
    private void deviceAddedHandler(int productID) {
        m_deviceAddedListeners.stream().forEach(l -> l.deviceAdded(productID));
    }

    /**
     *  deviceRemovedHandler calls the registered listeners.
     * @param productID id
     */
    private void deviceRemovedHandler(int productID) {
        m_deviceRemovedListeners.stream().forEach(l -> l.deviceRemoved(productID));
    }
}