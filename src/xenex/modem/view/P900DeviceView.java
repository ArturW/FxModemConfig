/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.view;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


/**
 *
 * @author user
 */
class P900DeviceView extends VBox {

    Text macAddress = new Text("");
    Text product = new Text("");
    Text firmware = new Text("");
    Text countryCode = new Text("");
        
    ComboBox<String> portBox = new ComboBox();    
    Button readBtn = new Button("Read");
    
    P900DeviceView() {        
        setupView();                                
    }
    
    private void setupView() {
        
        Text macAddressText = new Text("MAC Address: ");
        Text productText= new Text("Product: ");
        Text firmwareText = new Text("Firmware: ");
        Text countryCodeText = new Text("Country Code: ");
        
        
        GridPane deviceGrid = new GridPane();
        //deviceGrid.getStyleClass().add("gridpane");
        
        deviceGrid.add(macAddressText, 0, 0);
        deviceGrid.add(productText, 0, 1);
        deviceGrid.add(firmwareText, 0, 2);
        deviceGrid.add(countryCodeText, 0, 3);
        
        deviceGrid.add(macAddress, 1, 0);
        deviceGrid.add(product, 1, 1);
        deviceGrid.add(firmware, 1, 2);
        deviceGrid.add(countryCode, 1, 3);
             
        //deviceGrid.add(portBox, 2, 1);
        //deviceGrid.add(readBtn, 2, 3);
        
        ColumnConstraints textCol = new ColumnConstraints(130);
        textCol.setHalignment(HPos.RIGHT);
        ColumnConstraints valueCol = new ColumnConstraints(130);
                
        deviceGrid.getColumnConstraints().addAll(textCol, valueCol);        
        VBox leftBox = new VBox(deviceGrid);
        leftBox.setMaxWidth(300);
        leftBox.setMinWidth(300);
        
        
        AnchorPane buttonsPane = new AnchorPane();
        buttonsPane.getChildren().addAll(portBox, readBtn);
        
        AnchorPane.setRightAnchor(portBox, 5.0);
        AnchorPane.setTopAnchor(portBox, 10.0);
        AnchorPane.setRightAnchor(readBtn, 5.0);
        AnchorPane.setTopAnchor(readBtn, 45.0);
        VBox rightBox = new VBox(buttonsPane);
                
        HBox deviceBox = new HBox(5, leftBox, rightBox);                
        BorderTitlePane devicePane = new BorderTitlePane("Device", deviceBox);
        this.getChildren().add(devicePane);        
    }
}
