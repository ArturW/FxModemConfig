/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.view;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author user
 */
public class P900View extends StackPane {
    
    ProgressIndicator indicator = new ProgressIndicator();
    //ProgressBar progress = new ProgressBar(0);
    VBox views = new VBox();
    //private final P900 modem;
    P900DeviceView device = new P900DeviceView();
    P900FactoryDefaultsView factory = new P900FactoryDefaultsView();
    P900SettingsView settings = new P900SettingsView();
    
    public P900View() {
        //this.modem = (P900)model;
        indicator.setScaleX(0.6);
        indicator.setScaleY(0.6);
        
        views.getChildren().addAll(device, factory, settings);
      
        this.getChildren().add(views);
    }
    
    
}
