/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.view;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author user
 */
public class P900SettingsView extends VBox {

    private static final int MAX32 = 65535;
    
    final Text mode = new Text();
    final Text networkAddress = new Text();
    final Text unitAddress = new Text();
    final Text destinationAddress = new Text();
    final Text roamingAddress = new Text();
    
    final Text linkRate = new Text();
    final Text power = new Text();
    final Text hopPattern = new Text();
    final Text encryptionKey = new Text();
    final Text encryptionMode = new Text();
    
    final Text baudRate = new Text();
    final Text dataFormat = new Text();
    final Text channelMode = new Text();
        
    final Spinner<Integer> networkAddressSpinner = new Spinner<>(0, Integer.MAX_VALUE, 1234567890, 1);
    final Spinner<Integer> unitAddressSpinner = new Spinner<>(1, MAX32, 2);    
    final Spinner<Integer> roamingAddressSpinner = new Spinner<>(1, MAX32, 1);
    
    final Spinner<Integer> linkRateSpinner = new Spinner<>(0, 4, 0);
    final Spinner<Integer> powerSpinner = new Spinner<>(20, 30, 30);
    final Spinner<Integer> hopPatternSpinner = new Spinner<>(0, 49, 0);
    final Spinner<Integer> encryptionKeySpinner = new Spinner<>();
    final Spinner<Integer> encryptionModeSpinner = new Spinner<>();
    
    final Spinner<Integer> baudRateSpinner = new Spinner<>(0, 14, 7);
    final Spinner<Integer> dataFormatSpinner = new Spinner<>(1, 10, 1);
    final Spinner<Integer> channelModeSpinner = new Spinner<>(0, 2, 0);
      
    final CheckBox networkAddressCheckBox = new CheckBox();
    final CheckBox unitAddressCheckBox = new CheckBox(); 
    final CheckBox roamingAddressCheckBox = new CheckBox(); 
        
    final CheckBox linkRateCheckBox = new CheckBox(); 
    final CheckBox powerCheckBox = new CheckBox(); 
    final CheckBox hopPatternCheckBox = new CheckBox(); 
    final CheckBox encryptionKeyCheckBox = new CheckBox(); 
    final CheckBox encryptionModeCheckBox = new CheckBox();
    
    final CheckBox baudRateCheckBox = new CheckBox(); 
    final CheckBox dataFormatCheckBox = new CheckBox(); 
    final CheckBox channelModeCheckBox = new CheckBox();
    
    final Button saveBtn = new Button("PGM/Save");
    
    P900SettingsView() {
        setupView();        
        encryptionKeyCheckBox.setDisable(true);
        encryptionModeCheckBox.setDisable(true);
    }
    
    private void setupView() {
          
        networkAddressSpinner.setEditable(true);
        networkAddressSpinner.setDisable(true);
        unitAddressSpinner.setEditable(true);
        unitAddressSpinner.setDisable(true);
        roamingAddressSpinner.setEditable(true);
        roamingAddressSpinner.setDisable(true);
        linkRateSpinner.setDisable(true);
        powerSpinner.setDisable(true);
        hopPatternSpinner.setDisable(true);
        encryptionKeySpinner.setDisable(true);
        encryptionModeSpinner.setDisable(true);
        baudRateSpinner.setDisable(true);
        dataFormatSpinner.setDisable(true);
        channelModeSpinner.setDisable(true);

        final Text parameterText = new Text("Parameter");
        final Text currentValueText = new Text("Current Value");
        final Text modifyText = new Text("Modify");
        final Text newValueText = new Text("New Value");
        
        final GridPane headerGrid = new GridPane();
        headerGrid.getStyleClass().add("border-header");
        headerGrid.add(parameterText, 0, 0);
        headerGrid.add(currentValueText, 1 ,0);
        headerGrid.add(modifyText, 2, 0);
        headerGrid.add(newValueText, 3, 0);        
        
        final Text modeText = new Text("Mode of Operation");
        final Text networkAddressText = new Text("Network Address");
        final Text unitAddressText = new Text("Unit Address");
        final Text destinationAddressText = new Text("Destination Address");
        final Text roamingAddressText = new Text("Roaming Address");
        
        
        networkAddressCheckBox.getStyleClass().add("check-box");        
                
        final GridPane addressGrid = new GridPane();
        addressGrid.getStyleClass().add("gridpane");
        addressGrid.getStyleClass().add("border");
        addressGrid.add(modeText, 0, 0);
        addressGrid.add(networkAddressText, 0, 1);
        addressGrid.add(unitAddressText, 0, 2);
        addressGrid.add(destinationAddressText, 0, 3);
        addressGrid.add(roamingAddressText, 0, 4);
                  
        addressGrid.add(mode, 1, 0);
        addressGrid.add(networkAddress, 1, 1);
        addressGrid.add(unitAddress, 1, 2);
        addressGrid.add(destinationAddress, 1, 3);
        addressGrid.add(roamingAddress, 1, 4);
        
        addressGrid.add(networkAddressCheckBox, 2, 1);
        addressGrid.add(unitAddressCheckBox, 2, 2);
        addressGrid.add(roamingAddressCheckBox, 2, 4);
                
        addressGrid.add(networkAddressSpinner, 3, 1);
        addressGrid.add(unitAddressSpinner, 3, 2);
        addressGrid.add(roamingAddressSpinner, 3, 4);
        
        final Text linkRateText = new Text("Wireless Link Rate");
        final Text powerText = new Text("Power");
        final Text hopPatternText = new Text("Hop Pattern");
        final Text encryptionKeyText = new Text("Encryption Key");
        final Text encryptionModeText = new Text("Encryption Mode");
            
        
        
        final GridPane radioGrid = new GridPane();
        radioGrid.getStyleClass().add("gridpane");
        radioGrid.getStyleClass().add("border");
        
        radioGrid.add(linkRateText, 0, 0);
        radioGrid.add(powerText, 0, 1);
        radioGrid.add(hopPatternText, 0, 2);
        radioGrid.add(encryptionKeyText, 0, 3);
        radioGrid.add(encryptionModeText, 0, 4);
        
        radioGrid.add(linkRate, 1, 0);
        radioGrid.add(power, 1, 1);
        radioGrid.add(hopPattern, 1, 2);
        radioGrid.add(encryptionKey, 1, 3);
        radioGrid.add(encryptionMode, 1, 4);
        
        radioGrid.add(linkRateCheckBox, 2, 0);
        radioGrid.add(powerCheckBox, 2, 1);
        radioGrid.add(hopPatternCheckBox, 2, 2);
        radioGrid.add(encryptionKeyCheckBox, 2, 3);
        radioGrid.add(encryptionModeCheckBox, 2, 4);
        
        radioGrid.add(linkRateSpinner, 3, 0);
        radioGrid.add(powerSpinner, 3, 1);
        radioGrid.add(hopPatternSpinner, 3, 2);
        radioGrid.add(encryptionKeySpinner, 3, 3);
        radioGrid.add(encryptionModeSpinner, 3, 4);
        
        final Text baudRateText = new Text("Serial Baud Rate");
        final Text dataFormatText = new Text("Serial Data Format");
        final Text channelModeText = new Text("Serial Channel Mode");               
       
        final GridPane commsGrid = new GridPane();
        commsGrid.getStyleClass().add("gridpane");
        commsGrid.getStyleClass().add("border");
        
        commsGrid.add(baudRateText, 0, 0);
        commsGrid.add(dataFormatText, 0, 1);
        commsGrid.add(channelModeText, 0, 2);
     
        commsGrid.add(baudRate, 1, 0);
        commsGrid.add(dataFormat, 1, 1);
        commsGrid.add(channelMode, 1, 2);
        
        commsGrid.add(baudRateCheckBox, 2, 0);
        commsGrid.add(dataFormatCheckBox, 2, 1);
        commsGrid.add(channelModeCheckBox, 2, 2);
      
        commsGrid.add(baudRateSpinner, 3, 0);
        commsGrid.add(dataFormatSpinner, 3, 1);
        commsGrid.add(channelModeSpinner, 3, 2);
                
        /*
        IntStream.range(0, 4)
                .forEach(i -> {
                    ColumnConstraints column = new ColumnConstraints();
                    column.setHgrow(Priority.ALWAYS);                    
                    headerGrid.getColumnConstraints().addAll(column);
                    addressGrid.getColumnConstraints().addAll(column);
                    radioGrid.getColumnConstraints().addAll(column);
                    commsGrid.getColumnConstraints().addAll(column);
                });
        */
              
        ColumnConstraints parameterCol = new ColumnConstraints(150);              
        ColumnConstraints currentValCol = new ColumnConstraints(110);
        ColumnConstraints modifyCol = new ColumnConstraints(50);
        modifyCol.setHalignment(HPos.CENTER);
        ColumnConstraints newValCol = new ColumnConstraints(140);
        //newValCol.setPercentWidth(10);  // didnt work well         
        headerGrid.getColumnConstraints().addAll(parameterCol, currentValCol, modifyCol, newValCol);
        addressGrid.getColumnConstraints().addAll(parameterCol, currentValCol, modifyCol, newValCol);
        radioGrid.getColumnConstraints().addAll(parameterCol, currentValCol, modifyCol, newValCol);
        commsGrid.getColumnConstraints().addAll(parameterCol, currentValCol, modifyCol, newValCol);
        
        AnchorPane savePane = new AnchorPane();
        savePane.getChildren().addAll(saveBtn);
        AnchorPane.setTopAnchor(saveBtn, 5.0);
        AnchorPane.setRightAnchor(saveBtn, 5.0);
                
        final VBox settingsBox = new VBox(headerGrid, addressGrid, radioGrid, commsGrid, savePane);
        final BorderTitlePane defaultsPane = new BorderTitlePane("Settings", settingsBox);
        this.getChildren().add(defaultsPane);
    }
    
    
    
}
