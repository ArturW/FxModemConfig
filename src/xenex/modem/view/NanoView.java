/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.view;

import javafx.scene.layout.VBox;

/**
 *
 * @author user
 */
public class NanoView extends VBox {
    
    
    //private final P900 modem;
    NanoDeviceView device = new NanoDeviceView();
    NanoFactoryDefaultsView factory = new NanoFactoryDefaultsView();
    NanoSettingsView settings = new NanoSettingsView();
    
    public NanoView() {
        //this.modem = (P900)model;
        this.getChildren().addAll(device, factory, settings);
      
    }
}
