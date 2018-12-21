/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.view;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import jssc.SerialPortException;
import xenex.modem.nano.NanoCommandId;
import xenex.modem.nano.Nano;
import xenex.modem.nano.NanoDefaults;
import xenex.modem.nano.NanoParamId;
import xenex.serial.Serial;

/**
 *
 * @author user
 */
public class NanoPresenter {
    private static final int MAX32 = 65535;
    
    private final NanoView view;
    //private final Nano nano;
    private final StringBuilder portName = new StringBuilder();
    
    NanoPresenter(NanoView view) {
        this.view = view;
        //this.nano = nano;
        
        portLookup();
        attachEvents();
    }
    
    private void portLookup() {
        System.out.println("Looking for ports...");
        List<String> ports = Serial.getPortNames();
        System.out.println("Found ports... " + ports);
        ports.stream()
                .forEach(each -> view.device.portBox.getItems().add(each));
        view.device.portBox.setValue("Ports");
    }
    
    private void attachEvents() {
        view.device.portBox.setOnAction(value -> {            
            String name = view.device.portBox.getValue();            
            portName.replace(0, portName.length(), name);
            System.out.println("Selected port..." + portName);                        
            
        });                
                
        view.device.readBtn.setOnAction(value -> {
            try {
                readSettings();
            } catch (Exception ex) {
                Logger.getLogger(NanoPresenter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        view.factory.defaultBtn.setOnAction(value -> {
            String str = view.factory.defaultCB.getValue();
            str = str.replace(" ", "_").toUpperCase();
            NanoDefaults mode = NanoDefaults.valueOf(str);
            System.out.println("Loading defaults... " + mode.toString());         
            
            try {
                Serial port = Serial.newInstance(portName.toString());
                port.send(NanoCommandId.DEFAULTS, mode.getMode());                        
                port.disconnect();
            } catch (SerialPortException ex) {
                Logger.getLogger(NanoPresenter.class.getName()).log(Level.SEVERE, null, ex);
            } 

            sleep(2000);            
            try {
                readSettings();
            } catch (SerialPortException ex) {
                Logger.getLogger(NanoPresenter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
                
        view.settings.networkAddressCheckBox.setOnAction(value -> {
            //view.settings.networkAddressCheckBox.setSelected(true);
        });
        
        view.settings.networkAddressCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.networkAddressCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.networkAddressSpinner.setDisable(!newValue);
            } else {
                view.settings.networkAddressSpinner.setDisable(!newValue);
            }
        });        
        /*
        view.settings.networkAddressSpinner.valueProperty().addListener((listener, oldValue, newValue) -> {
            System.out.println(newValue);
        });
        
        view.settings.networkAddressSpinner.getEditor().setOnAction(Value -> {             
        });
        
        */
        view.settings.unitAddressCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.unitAddressCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.unitAddressSpinner.setDisable(!newValue);
            } else {
                view.settings.unitAddressSpinner.setDisable(!newValue);
            }
        });                
        
        view.settings.roamingAddressCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.roamingAddressCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.roamingAddressSpinner.setDisable(!newValue);
            } else {
                view.settings.roamingAddressSpinner.setDisable(!newValue);
            }
        });
        
        view.settings.linkRateCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.linkRateCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.linkRateSpinner.setDisable(!newValue);
            } else {
                view.settings.linkRateSpinner.setDisable(!newValue);
            }
        });
        
        view.settings.powerCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.powerCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.powerSpinner.setDisable(!newValue);
            } else {
                view.settings.powerSpinner.setDisable(!newValue);
            }
        });
       
        /*        
        view.settings.encryptionKeyCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.encryptionKeyCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.encryptionKeySpinner.setDisable(!newValue);
            } else {
                view.settings.encryptionKeySpinner.setDisable(!newValue);
            }
        });
        
        view.settings.encryptionModeCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.encryptionModeCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.encryptionModeSpinner.setDisable(!newValue);
            } else {
                view.settings.encryptionModeSpinner.setDisable(!newValue);
            }
        });
        */
        view.settings.baudRateCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.baudRateCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.baudRateSpinner.setDisable(!newValue);
            } else {
                view.settings.baudRateSpinner.setDisable(!newValue);
            }
        });
        
        view.settings.dataFormatCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.dataFormatCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.dataFormatSpinner.setDisable(!newValue);
            } else {
                view.settings.dataFormatSpinner.setDisable(!newValue);
            }
        });
        
        view.settings.channelModeCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.channelModeCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.channelModeSpinner.setDisable(!newValue);
            } else {
                view.settings.channelModeSpinner.setDisable(!newValue);
            }
        });
        
        view.settings.saveBtn.setOnAction(value -> {
            List<Byte> parametersList = new LinkedList<>();
            if (view.settings.networkAddressCheckBox.isSelected()) {
                int param = view.settings.networkAddressSpinner.getValue();
                byte[] buffer = ByteBuffer.allocate(8).putInt(param).array();
                parametersList.add(NanoParamId.NETWORK_ID_H.getId());
                parametersList.add(buffer[0]);
                parametersList.add(NanoParamId.NETWORK_ID_M2.getId());
                parametersList.add(buffer[1]);
                parametersList.add(NanoParamId.NETWORK_ID_M1.getId());
                parametersList.add(buffer[2]);
                parametersList.add(NanoParamId.NETWORK_ID_L.getId());
                parametersList.add(buffer[3]);
                //System.out.println(Arrays.toString(buffer));
                
            }
            
            //byte[] addressBuffer = new byte[] {0, 0, Byte.parseByte(settings.get(NanoParamId.UNIT_ADDRESS_H)),
            //Byte.parseByte(settings.get(NanoParamId.UNIT_ADDRESS_L))};
            //int unitAddress = ByteBuffer.wrap(addressBuffer).getInt();
            if (view.settings.unitAddressCheckBox.isSelected()) {
                int param = view.settings.unitAddressSpinner.getValue();
                byte[] buffer = ByteBuffer.allocate(4).putInt(param).array();
                parametersList.add(NanoParamId.UNIT_ADDRESS_H.getId());
                parametersList.add(buffer[1]);
                parametersList.add(NanoParamId.UNIT_ADDRESS_L.getId());
                parametersList.add(buffer[3]);
                //System.out.println(Arrays.toString(buffer));
            }
            
            if (view.settings.roamingAddressCheckBox.isSelected()) {
                int param = view.settings.roamingAddressSpinner.getValue();
                byte[] buffer = ByteBuffer.allocate(4).putInt(param).array();
                parametersList.add(NanoParamId.ROAMING_ADDRESS_H.getId());
                parametersList.add(buffer[1]);
                parametersList.add(NanoParamId.ROAMING_ADDRESS_L.getId());
                parametersList.add(buffer[3]);
                //System.out.println(Arrays.toString(buffer));
            }          
            
            if (view.settings.linkRateCheckBox.isSelected()) {
                int param = view.settings.linkRateSpinner.getValue();
                parametersList.add(NanoParamId.LINK_RATE.getId());
                parametersList.add((byte)param);
            }
            
            if (view.settings.powerCheckBox.isSelected()) {
                int param = view.settings.powerSpinner.getValue();
                parametersList.add(NanoParamId.POWER.getId());
                parametersList.add((byte)param);
            }
            
            if (view.settings.baudRateCheckBox.isSelected()) {
                int param = view.settings.baudRateSpinner.getValue();
                parametersList.add(NanoParamId.BAUD_RATE.getId());
                parametersList.add((byte)param);
            }
            
            if (view.settings.dataFormatCheckBox.isSelected()) {
                int param = view.settings.dataFormatSpinner.getValue();
                parametersList.add(NanoParamId.DATA_FORMAT.getId());
                parametersList.add((byte)param);
            }
            
            if (view.settings.channelModeCheckBox.isSelected()) {
                int param = view.settings.channelModeSpinner.getValue();
                parametersList.add(NanoParamId.CHANNEL_MODE.getId());
                parametersList.add((byte)param);
            }
            
            System.out.println("Updating settings... " + parametersList);
            byte[] parameters = new byte[parametersList.size()];
            IntStream.range(0, parametersList.size())
                    .forEach(i -> parameters[i] = parametersList.get(i));
            
            try {
                Serial port = Serial.newInstance(portName.toString());
                port.send(NanoCommandId.SET_PARAMETERS, parameters);            
                port.disconnect();
            } catch (SerialPortException ex) {
                Logger.getLogger(NanoPresenter.class.getName()).log(Level.SEVERE, null, ex);
            }
                        
            sleep(1000);
            try {
                readSettings();
            } catch (SerialPortException ex) {
                Logger.getLogger(NanoPresenter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });                
    }
    
    private boolean checkField(Spinner spinner, String oldValue, String newValue ) {            
            if (newValue.matches("[0-9]*")) {                
                //field.setText(newValue);
                return true;
            } else {
                //field.setText(oldValue);
                return false;
            }
    }
    
    private void checkFieldLength(TextField field) {
        if (field.getText().length() > 5) {
            field.setText(field.getText().substring(0, 5));
        }
    }
    
    private void readSettings() throws SerialPortException {
        System.out.println("Reading device...");
        Serial port = Serial.newInstance(portName.toString());

        Nano modem = port.readNanoSettings();
        port.disconnect();

        view.device.serialNumber.setText(modem.getSerialNumber());
        view.device.product.setText(modem.getProduct());
        view.device.firmware.setText(modem.getFirmware());
        view.device.countryCode.setText(modem.getCountryCode());

        view.settings.mode.setText(modem.getModeOfOperation());
        view.settings.networkAddress.setText(modem.getNetworkAddress());
        view.settings.unitAddress.setText(modem.getUnitAddress());
        view.settings.destinationAddress.setText(modem.getDestinationAddress());
        view.settings.roamingAddress.setText(modem.getRoamingAddress());
        
        view.settings.linkRate.setText(modem.getWirelessLinkRate());      
        view.settings.power.setText(modem.getPower());
        
        view.settings.baudRate.setText(modem.getSerialBaudrRate());
        view.settings.dataFormat.setText(modem.getSerialDataFormat());
        view.settings.channelMode.setText(modem.getSerialChannelMode());

        System.out.println(modem); 
    }
    
    
    private void sleep(long value) {
        try {
            Thread.sleep(value);
        } catch (InterruptedException ex) {
            Logger.getLogger(NanoPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
