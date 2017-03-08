/**
 * de.hjbflyer.connexion3dapi
 * ConnexionAPIDemo.java
 * copyright(c) hjb 06.03.2017
 */
package de.hjbflyer.connexion3dapi;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ConnexionAPIDemo is a simple demo to show the usage of the API
 * 
 * @author hjbflyer
 * 
 * Copyright(c) hjbflyer 2017
 *
 */
public class ConnexionAPIDemo extends Application
    implements IConnexionAxisChangedListener, IConnexionButtonChangedListener,
    IConnexionDeviceAddedListener, IConnexionDeviceRemovedListener, IConnexionPrefsChangedListener {

    /** APP_NAME name of the application */
    private static final String APP_NAME = "3DConnexionAPIDemo"; //$NON-NLS-1$

    /**
     * Run the test program as simple Java FX application.
     * 
     * @param args command line
     */
    public static void main(String[] args) {
        launch(args);
    }

    /** 
     * {@InheritDoc}
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ConnexionDevicePrefs prefs = new ConnexionDevicePrefs();
        
        ConnexionAPI api = new ConnexionAPI(APP_NAME);
        api.addAxisChangedListener(this);
        api.addButtonChangedListener(this);
        api.addDeviceAddedListeners(this);
        api.addDeviceRemovedListeners(this);
        api.addPrefsChangedListeners(this);

        primaryStage.setTitle(APP_NAME);
        Button btn = new Button();

        btn.setText("Press button to request device prefs"); //$NON-NLS-1$
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                api.connexionGetCurrentDevicePrefs(prefs);
                System.out.println("Prefs: " + prefs.toString());
                Integer i = new Integer(0);
                int err = api.connexionClientControl(ConnexionAPI.CTRL_GET_CLIENT_ID, 0, i);
                System.out.println("control returned with error: " + err + " and clientID " + i);  //$NON-NLS-1$//$NON-NLS-2$
            }
        });
        Button btn2 = new Button();
        btn2.setText("Exit program"); //$NON-NLS-1$
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                api.closeAndCleanUp();
                System.exit(0);
            }
        });
        
        VBox root = new VBox();
        root.getChildren().addAll(btn, btn2);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    /** 
     * {@InheritDoc}
     * @see de.hjbflyer.connexion3dapi.IConnexionPrefsChangedListener#prefsChanged(de.hjbflyer.connexion3dapi.ConnexionDevicePrefs)
     */
    @Override
    public void prefsChanged(ConnexionDevicePrefs prefs) {
        System.out.println("Prefs changed: " + prefs.toString()); //$NON-NLS-1$
    }

    /** 
     * {@InheritDoc}
     * @see de.hjbflyer.connexion3dapi.IConnexionDeviceRemovedListener#deviceRemoved(int)
     */
    @Override
    public void deviceRemoved(int id) {
        System.out.println("3DConnexion device with ID: " + id + " removed from system");  //$NON-NLS-1$//$NON-NLS-2$
        
    }

    /** 
     * {@InheritDoc}
     * @see de.hjbflyer.connexion3dapi.IConnexionDeviceAddedListener#deviceAdded(int)
     */
    @Override
    public void deviceAdded(int id) {
        System.out.println("3DConnexion device with ID: " + id + " added to system");  //$NON-NLS-1$//$NON-NLS-2$
    }

    /** 
     * {@InheritDoc}
     * @see de.hjbflyer.connexion3dapi.IConnexionButtonChangedListener#buttonChanged(long)
     */
    @Override
    public void buttonChanged(long button) {
        System.out.println("3DConnexion buttons changed: " + Long.toHexString(button)); //$NON-NLS-1$
    }

    /** 
     * {@InheritDoc}
     * @see de.hjbflyer.connexion3dapi.IConnexionAxisChangedListener#axisChanged(int[])
     */
    @Override
    public void axisChanged(int[] values) {
        String[] axis = new String[]{"x=", ", y=", ", z=", ", rx=", ", ry=", ", rz="};      //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$//$NON-NLS-6$
        for (int i = 0; i < ConnexionAPI.AXIS; i++) {
            System.out.print(axis[i] + values[i]);
        }
        System.out.println();
    }
}
