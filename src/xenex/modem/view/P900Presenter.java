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
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import jssc.SerialPortException;
import xenex.modem.ModemType;
import xenex.modem.pico900.P900;
import xenex.modem.pico900.P900Defaults;
import xenex.modem.pico900.P900Packet;
import xenex.modem.pico900.P900PacketBuilder;
import xenex.modem.pico900.P900ParamId;
import xenex.serial.Serial;
import xenex.serial.exceptions.P900Exception;
import xenex.util.ArrayUtils;

/**
 *
 * @author user
 */
public class P900Presenter {
    private static final int MAX32 = 65535;
    private static final String PWD = "abc";
    
    private final P900View view;
    private final StringBuilder portName = new StringBuilder();
    
    P900Presenter(P900View view) {
        this.view = view;
        
        portLookup();
        attachEvents();
    }
        
    private void portLookup() {
        
        StringBuilder selection = new StringBuilder();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {               
                System.out.println("Looking for ports...");                
                view.views.setDisable(true);
                
                List<String> ports = Serial.getPortNames();
                System.out.println("Found ports... " + ports);

                ports.stream()
                        .forEach(each -> {
                            view.device.portBox.getItems().add(each);
                            if (each.contains("/ttyUSB")) {                                                                
                                System.out.println("Looking for device connecteed to -> " + each);
                                String name = modemLookup(each);
                                System.out.println("Identifying -> " + name);
                                if (name != null && !name.isEmpty()) {
                                    System.out.println("Found modem connected to -> "  + name);
                                    if (ModemType.P900.equals(ModemType.valueOf(name.toUpperCase()))) {
                                        // not on same thread
                                        //view.device.portBox.setValue(each);
                                        selection.append(each);                                        
                                        portName.replace(0, portName.length(), each);      
                                        
                                        try {
                                            readSettings();
                                        } catch (P900Exception ex) {
                                            Logger.getLogger(P900Presenter.class.getName()).log(Level.SEVERE, null, ex);
                                            Task exceptionTask = new Task() {
                                                @Override
                                                protected Object call() throws Exception {                                
                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                    alert.setTitle("Error");
                                                    alert.setHeaderText("Error Reading Settings");
                                                    alert.setContentText(ex.getMessage());
                                                    alert.showAndWait();
                                                    return null;
                                                    }
                                                };
                                            Platform.runLater(exceptionTask);
                                        }
                                    }
                                }
                            }
                        });                
                view.views.setDisable(false);
                
                // Modification to view (portBox) has to be done on main thread
                Task setSelectionTask = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        view.views.setDisable(true);
                        // This also performs event action
                        view.device.portBox.setValue(selection.toString());                        
                        view.getChildren().remove(view.indicator);
                        view.views.setDisable(false);
                        return null;                        
                    }                    
                };
                
                Platform.runLater(setSelectionTask);
                return null;
            }                          
        };
        
        view.getChildren().add(view.indicator);
        view.indicator.progressProperty().bind(task.progressProperty());
            
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();   
    }
    
    private String modemLookup(String portName)  {
        
        
        
        Serial port = null;
        String name = null;
        try {
            port = Serial.newInstance(portName);
            port.login(PWD);
            
            byte[] command = new byte[] {P900ParamId.PRODUCT_STRING.getId()};                
            P900Packet txPacket = new P900PacketBuilder().setRead(command).create();
            P900Packet rxPacket = port.send(txPacket);
            
            byte[] data = rxPacket.getData();
            name = rxPacket.getDataAsString(data);                        
        } catch (SerialPortException ex) {
            System.err.println("Failed to opend com port=" + portName );
            Task exceptionTask = new Task() {
                @Override
                protected Object call() throws Exception {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to opend com port=" + portName );
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    return null;
                }            
            };
            Platform.runLater(exceptionTask);
        } catch (P900Exception ex) {
            System.err.println(ex);
        } finally {
            if (port != null)
                port.disconnect();
        }
        return name;
    }
    
    private void attachEvents() {
        /*
        view.device.portBox.setOnAction(value -> {
            
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {                    
                    view.views.setDisable(true);
                    
                    String name = view.device.portBox.getValue();            
                    portName.replace(0, portName.length(), name);
                    System.out.println("Selected port..." + portName);

                    try {
                        readSettings();
                    } catch (Exception ex) {
                        Logger.getLogger(P900Presenter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    view.views.setDisable(false);
                    
                    Task removeIndicatorTask = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            view.getChildren().remove(view.indicator);
                            return 0;
                        }                    
                    };
                    
                    Platform.runLater(removeIndicatorTask);
                    return null;
                }            
            };
            
            view.getChildren().add(view.indicator);
            view.indicator.progressProperty().bind(task.progressProperty());
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start(); 
        });        
        */  
        view.device.readBtn.setOnAction(value -> {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {                    
                    view.views.setDisable(true);                    
                    try {                
                        readSettings();
                    } catch (P900Exception ex) {
                        Logger.getLogger(P900Presenter.class.getName()).log(Level.SEVERE, null, ex);
                        Task exceptionTask = new Task() {
                            @Override
                            protected Object call() throws Exception {                                
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Error Reading Settings");
                                alert.setContentText(ex.getMessage());
                                alert.showAndWait();
                                return null;
                            }
                        };
                        Platform.runLater(exceptionTask);
                    }
                    view.views.setDisable(false);
                    
                    Task removeIndicatorTask = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            view.getChildren().remove(view.indicator);
                            return 0;
                        }                    
                    };                    
                    Platform.runLater(removeIndicatorTask);
                    return null;
                }
                
            };    
            
            view.getChildren().add(view.indicator);
            view.indicator.progressProperty().bind(task.progressProperty());
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();  
        });
        
        view.factory.defaultBtn.setOnAction(value -> {
            
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {                    
                    view.views.setDisable(true);
                    
                    String str = view.factory.defaultCB.getValue();                                        
                    str = str.replace(" ", "_").toUpperCase();
                    P900Defaults mode = P900Defaults.valueOf(str);
                    System.out.println("Loading defaults... " + mode.toString());         
                    byte arg = mode.getMode();
                    byte [] payload = new byte[] {
                        P900ParamId.FACTORY_DEFAULTS.getId(), arg
                    };
                    //port.send(P900CommandId.DEFAULTS, mode.getMode());                        
                    P900Packet txPacket = new P900PacketBuilder()
                            .setWrite(payload)                    
                            .create();

                    Serial port = null;

                    try {
                        port = Serial.newInstance(portName.toString());
                        port.login(PWD);
                        P900Packet rxPacket = port.send(txPacket);            
                        port.saveProxyParameters();
                        port.disconnect();

                        sleep(3000);

                        readSettings();
                    } catch (SerialPortException ex) {
                        Task exceptionTask = new Task() {
                            @Override
                            protected Object call() throws Exception {   
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Failed to opend com port=" + portName );
                                alert.setContentText(ex.getMessage());
                                alert.showAndWait();
                                return null;
                            }
                        };
                        Platform.runLater(exceptionTask);
                    } catch (P900Exception ex) {
                        Task exceptionTask = new Task() {
                            @Override
                            protected Object call() throws Exception {   
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Error Loading Default Settings");
                                alert.setContentText(ex.getMessage());
                                alert.showAndWait();
                                return null;
                            }
                        };
                        Platform.runLater(exceptionTask);
                    }
                    
                    view.views.setDisable(false);                    
                    
                    Task removeIndicatorTask = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            view.getChildren().remove(view.indicator);
                            return 0;
                        }                    
                    };
                    
                    Platform.runLater(removeIndicatorTask);
                    return null;
                }            
            };
                                    
            view.getChildren().add(view.indicator);
            view.indicator.progressProperty().bind(task.progressProperty());
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();
            
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
        views.settings.networkAddressSpinner.valueProperty().addListener((listener, oldValue, newValue) -> {
            System.out.println(newValue);
        });
        
        views.settings.networkAddressSpinner.getEditor().setOnAction(Value -> {             
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
        
        view.settings.hopPatternCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            view.settings.hopPatternCheckBox.setSelected(newValue);
            if (newValue) {
                view.settings.hopPatternSpinner.setDisable(!newValue);
            } else {
                view.settings.hopPatternSpinner.setDisable(!newValue);
            }
        });
        /*
        views.settings.encryptionKeyCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            views.settings.encryptionKeyCheckBox.setSelected(newValue);
            if (newValue) {
                views.settings.encryptionKeySpinner.setDisable(!newValue);
            } else {
                views.settings.encryptionKeySpinner.setDisable(!newValue);
            }
        });
        
        views.settings.encryptionModeCheckBox.selectedProperty().addListener((listener, oldValue, newValue ) -> {
            views.settings.encryptionModeCheckBox.setSelected(newValue);
            if (newValue) {
                views.settings.encryptionModeSpinner.setDisable(!newValue);
            } else {
                views.settings.encryptionModeSpinner.setDisable(!newValue);
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
            
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    System.out.println("Saving settings");                    
                    view.views.setDisable(true);
                    
                    List<Byte> parametersList = new LinkedList<>();
                    if (view.settings.networkAddressCheckBox.isSelected()) {
                        int param = view.settings.networkAddressSpinner.getValue();
                        byte[] buffer = ByteBuffer.allocate(4).putInt(param).array();
                        parametersList.add(P900ParamId.NETWORK_ADDRESS.getId());
                        parametersList.add(buffer[0]);
                        parametersList.add(buffer[1]);
                        parametersList.add(buffer[2]);
                        parametersList.add(buffer[3]);
                        parametersList.add(buffer[4]);
                        parametersList.add(buffer[5]);
                        parametersList.add(buffer[6]);
                        parametersList.add(buffer[7]);
                        //System.out.println(ArrayUtils.toHexString(buffer));

                    }

                    //byte[] addressBuffer = new byte[] {0, 0, Byte.parseByte(settings.get(NanoParamId.UNIT_ADDRESS_H)),
                    //Byte.parseByte(settings.get(NanoParamId.UNIT_ADDRESS_L))};
                    //int unitAddress = ByteBuffer.wrap(addressBuffer).getInt();
                    if (view.settings.unitAddressCheckBox.isSelected()) {
                        int param = view.settings.unitAddressSpinner.getValue();
                        byte[] buffer = ByteBuffer.allocate(4).putInt(param).array();
                        parametersList.add(P900ParamId.UNIT_ADDRESS.getId());                
                        parametersList.add(buffer[1]);                
                        parametersList.add(buffer[3]);
                        //System.out.println(ArrayUtils.toHexString(buffer));
                    }

                    if (view.settings.roamingAddressCheckBox.isSelected()) {
                        int param = view.settings.roamingAddressSpinner.getValue();
                        byte[] buffer = ByteBuffer.allocate(4).putInt(param).array();
                        parametersList.add(P900ParamId.ROAMING_ADDRESS.getId());                
                        parametersList.add(buffer[1]);                
                        parametersList.add(buffer[3]);
                        System.out.println(ArrayUtils.toHexString(buffer));
                    }          

                    if (view.settings.linkRateCheckBox.isSelected()) {
                        int param = view.settings.linkRateSpinner.getValue();
                        parametersList.add(P900ParamId.LINK_RATE.getId());
                        parametersList.add((byte)param);
                    }

                    if (view.settings.powerCheckBox.isSelected()) {
                        int param = view.settings.powerSpinner.getValue();
                        parametersList.add(P900ParamId.POWER.getId());
                        parametersList.add((byte)param);
                    }

                    if (view.settings.hopPatternCheckBox.isSelected()) {
                        int param = view.settings.hopPatternSpinner.getValue();
                        parametersList.add(P900ParamId.HOP_PATTERN.getId());
                        parametersList.add((byte)param);
                    }

                    if (view.settings.baudRateCheckBox.isSelected()) {
                        int param = view.settings.baudRateSpinner.getValue();
                        parametersList.add(P900ParamId.BAUD_RATE.getId());
                        parametersList.add((byte)param);
                    }

                    if (view.settings.dataFormatCheckBox.isSelected()) {
                        int param = view.settings.dataFormatSpinner.getValue();
                        parametersList.add(P900ParamId.DATA_FORMAT.getId());
                        parametersList.add((byte)param);
                    }

                    if (view.settings.channelModeCheckBox.isSelected()) {
                        int param = view.settings.channelModeSpinner.getValue();
                        parametersList.add(P900ParamId.CHANNEL_MODE.getId());
                        parametersList.add((byte)param);
                    }

                    System.out.println("Updating settings... " + parametersList);
                    byte[] parameters = new byte[parametersList.size()];
                    IntStream.range(0, parametersList.size())
                            .forEach(i -> parameters[i] = parametersList.get(i));
                    System.out.println(ArrayUtils.toHexString(parameters));

                    try {
                        P900Packet txPacket = new P900PacketBuilder()
                                .setWrite(parameters)
                                .create();

                        Serial port = Serial.newInstance(portName.toString());
                        port.login(PWD);

                        P900Packet rxPacket = port.send(txPacket);                                
                        port.disconnect();

                        sleep(1000);

                        readSettings();
                    } catch (SerialPortException ex) {
                        Logger.getLogger(P900Presenter.class.getName()).log(Level.SEVERE, null, ex);
                        Task exceptionTask = new Task() {
                            @Override
                            protected Object call() throws Exception {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Failed to opend com port=" + portName );
                                alert.setContentText(ex.getMessage());
                                alert.showAndWait();
                                return null;
                            }                            
                        };
                        Platform.runLater(exceptionTask);
                    } catch (P900Exception ex) {
                        Logger.getLogger(P900Presenter.class.getName()).log(Level.SEVERE, null, ex);
                        Task exceptionTask = new Task() {
                            @Override
                            protected Object call() throws Exception {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Error Updating Settings");
                                alert.setContentText(ex.getMessage());
                                alert.showAndWait();
                                return null;
                            }                            
                        };
                        Platform.runLater(exceptionTask);                        
                    }
                    
                    view.views.setDisable(false);
                    
                    Task removeIndicatorTask = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            view.getChildren().remove(view.indicator);
                            return 0;
                        }                    
                    };                    
                    Platform.runLater(removeIndicatorTask);
                    return null;
                }                                                                      
            };
            
            view.getChildren().add(view.indicator);
            view.indicator.progressProperty().bind(task.progressProperty());
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();  
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
    
    private void readSettings() throws P900Exception {
        System.out.println("Reading device...");
        
        Serial port = null;
        try {
            port = Serial.newInstance(portName.toString());
        
        } catch (SerialPortException ex) {
            Logger.getLogger(P900Presenter.class.getName()).log(Level.SEVERE, null, ex);
            Task exceptionTask = new Task() {
                @Override
                protected Object call() throws Exception {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to opend com port=" + portName );
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    return null;
                }            
            };
            Platform.runLater(exceptionTask);
            return;
        }
        
        port.login(PWD);        
        P900 modem = port.readP900Settings();
        port.disconnect();

        view.device.macAddress.setText(modem.getMacAddress());
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
        view.settings.hopPattern.setText(modem.getHopPattern());
        view.settings.encryptionMode.setText(modem.getEncryptionMode());
        
        view.settings.baudRate.setText(modem.getSerialBaudrRate());
        view.settings.dataFormat.setText(modem.getSerialDataFormat());
        view.settings.channelMode.setText(modem.getSerialChannelMode());

        System.out.println(modem); 
    }
    
    
    private void sleep(long value) {
        try {
            Thread.sleep(value);
        } catch (InterruptedException ex) {
            Logger.getLogger(P900Presenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
