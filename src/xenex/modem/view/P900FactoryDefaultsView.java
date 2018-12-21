/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.view;

import java.util.stream.Stream;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import xenex.modem.pico900.P900Defaults;

/**
 *
 * @author user
 */
class P900FactoryDefaultsView extends VBox {
    
    ComboBox<String> defaultCB = new ComboBox<>();
    Button defaultBtn = new Button("Default Unit");
    
    P900FactoryDefaultsView() {
        setupView();
    }
    
    private void setupView() {        
        String string = "Reset device to one of the Factory Defaults, "
                + "all customized settings on the device will be overridden.  "
                + "Mode of Operation can only be changed through factory settings.";
        Text text = new Text(string);
        text.setWrappingWidth(300);
        text.setFill(Color.DARKBLUE);
        //text.setStyle("-fx-text-fill: blue;");
        //TextFlow textFLow;
        VBox leftBox = new VBox(text);     
        leftBox.setMaxWidth(300);
        leftBox.setMinWidth(300);
        
        Stream.of(P900Defaults.values())
                .forEach(each -> defaultCB.getItems().add(each.getModeName()));
        defaultCB.setValue(P900Defaults.PMP_SLAVE.getModeName());
        defaultCB.setMaxWidth(150);
        defaultCB.setMinWidth(150);
        //defaultBtn.setMaxWidth(100);
        //defaultBtn.setMinWidth(100);
        
        AnchorPane buttonsPane = new AnchorPane();
        buttonsPane.getChildren().addAll(defaultCB, defaultBtn);
        
        AnchorPane.setRightAnchor(defaultCB, 5.0);
        AnchorPane.setTopAnchor(defaultCB, 10.0);
        AnchorPane.setRightAnchor(defaultBtn, 5.0);
        AnchorPane.setTopAnchor(defaultBtn, 45.0);
        VBox rightBox = new VBox(buttonsPane);
        
        HBox defaultsBox = new HBox(5, leftBox, rightBox);       
        BorderTitlePane defaultsPane = new BorderTitlePane("Load Factory Defaults", defaultsBox);
        this.getChildren().add(defaultsPane);
        
    }
    
}
