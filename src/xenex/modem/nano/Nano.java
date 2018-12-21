/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.nano;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import xenex.modem.Modem;

/**
 *
 * @author user
 */
public class Nano extends Modem{
    private static final int MAX16 = 65535;    
            
    private final ReadOnlyProperty<String> product;
    private final ReadOnlyProperty<String> countryCode;
    private final ReadOnlyProperty<String> firmware;
    
    private final ReadOnlyProperty<String> modeOfOperation;
    private final ReadOnlyProperty<String> networkAddress;
    private final ReadOnlyProperty<String> unitAddress;
    private final ReadOnlyProperty<String> destinationAddress;
    private final ReadOnlyProperty<String> roamingAddress;
    
    private final ReadOnlyProperty<String> wirelessLinkRate;
    private final ReadOnlyProperty<String> power;
    private final ReadOnlyProperty<String> hopPattern;
    private final ReadOnlyProperty<String> encryptionKey;
    private final ReadOnlyProperty<String> encryptionMode;
    
    private final ReadOnlyProperty<String> serialBaudrRate;
    private final ReadOnlyProperty<String> serialDataFormat;
    private final ReadOnlyProperty<String> serialChannelMode;

    Nano(String product, String countryCode, String firmware,
            String modeOfOperation, String networkAddress, String unitAddress, String destinationAddress, String roamingAddress,
            String wirelessLinkRate, String power, String hopPattern, String encryptionKey, String encryptionMode,
            String serialBaudrRate, String serialDataFormat, String serialChannelMode,
            String serialNumber) {
        super(serialNumber);        
        this.product = new ReadOnlyStringWrapper(product);
        this.countryCode = new ReadOnlyStringWrapper(countryCode);
        this.firmware = new ReadOnlyStringWrapper(firmware);
        this.modeOfOperation = new ReadOnlyStringWrapper(modeOfOperation);
        this.networkAddress = new ReadOnlyStringWrapper(networkAddress);
        this.unitAddress = new ReadOnlyStringWrapper(unitAddress);
        this.destinationAddress = new ReadOnlyStringWrapper(destinationAddress);
        this.roamingAddress = new ReadOnlyStringWrapper(roamingAddress);
        this.wirelessLinkRate = new ReadOnlyStringWrapper(wirelessLinkRate);
        this.power = new ReadOnlyStringWrapper(power);
        this.hopPattern = new ReadOnlyStringWrapper(hopPattern);
        this.encryptionKey = new ReadOnlyStringWrapper(encryptionKey);
        this.encryptionMode = new ReadOnlyStringWrapper(encryptionMode);
        this.serialBaudrRate = new ReadOnlyStringWrapper(serialBaudrRate);
        this.serialDataFormat = new ReadOnlyStringWrapper(serialDataFormat);
        this.serialChannelMode = new ReadOnlyStringWrapper(serialChannelMode);
    }

    public String getProduct() {
        return product.getValue();
    }
    
    public ReadOnlyProperty<String> product() {
        return product;
    }

    public String getCountryCode() {
        return countryCode.getValue();
    }
    
    public ReadOnlyProperty<String> countryCode() {
        return countryCode;
    }

    public String getFirmware() {
        return firmware.getValue();
    }

    public ReadOnlyProperty<String> firmware() {
        return firmware;
    }
    
    public String getModeOfOperation() {
        return modeOfOperation.getValue();
    }
    
    public ReadOnlyProperty<String> modeOfOperation() {
        return modeOfOperation;
    }

    public String getNetworkAddress() {
        return networkAddress.getValue();
    }
    
    public ReadOnlyProperty<String> networkAddress() {
        return networkAddress;
    }
    
    public String getUnitAddress() {
        return unitAddress.getValue();
    }

    public ReadOnlyProperty<String> unitAddress() {
        return unitAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress.getValue();
    }
    
    public ReadOnlyProperty<String> destinationAddress() {
        return destinationAddress;
    }

    public String getRoamingAddress() {
        return roamingAddress.getValue();
    }

    public ReadOnlyProperty<String> roamingAddress() {
        return roamingAddress;
    }
    
    public String getWirelessLinkRate() {
        return wirelessLinkRate.getValue();
    }
    
    public ReadOnlyProperty<String> wirelessLinkRate() {
        return wirelessLinkRate;
    }

    public String getPower() {
        return power.getValue();
    }

    public ReadOnlyProperty<String> power() {
        return power;
    }
    
    public String getHopPattern() {
        return hopPattern.getValue();
    }

    public ReadOnlyProperty<String> hopPattern() {
        return hopPattern;
    }
    
    public String getEncryptionKey() {
        return encryptionKey.getValue();
    }

    public ReadOnlyProperty<String> encryptionKey() {
        return encryptionKey;
    }
    
    public String getEncryptionMode() {
        return encryptionMode.getValue();
    }

    public ReadOnlyProperty<String> encryptionMode() {
        return encryptionMode;
    }
    
    public String getSerialBaudrRate() {
        return serialBaudrRate.getValue();
    }

    public ReadOnlyProperty<String> serialBaudrRate() {
        return serialBaudrRate;
    }
    
    public String getSerialDataFormat() {
        return serialDataFormat.getValue();
    }

    public ReadOnlyProperty<String> serialDataFormat() {
        return serialDataFormat;
    }
    
    public String getSerialChannelMode() {
        return serialChannelMode.getValue();
    }
    
    public ReadOnlyProperty<String> serialChannelMode() {
        return serialChannelMode;
    }

    /* Domain specific business rules */
	
    public boolean isValidNetowrkAddress(String address) {
        int value = Integer.parseInt(address);
        return value >= 0 && value <= 8000000000l;
    }
    
    public boolean isValidUnitAddress(String address) {
        int value = Integer.parseInt(address);
        return value >= 1 && value <= MAX16;
    }
    
    public boolean isValidDestinationAddress(String address) {
        int value = Integer.parseInt(address);
        return value >= 1 && value <= MAX16;
    }
    
    public boolean isValidRoamingAddress(String address) {
        int value = Integer.parseInt(address);
        return value >= 1 && value <= MAX16;
    }
    
    
    @Override
    public String toString() {
        return "Nano{" + super.toString() + ", product=" + product.getValue() + ", firmware=" + firmware.getValue() + ", countryCode=" + countryCode.getValue()
                + ", modeOfOperation=" + modeOfOperation.getValue() + ", networkAddress=" + networkAddress.getValue() + ", unitAddress=" + unitAddress.getValue() + ", destinationAddress=" + destinationAddress.getValue() + ", roamingAddress=" + roamingAddress.getValue()
                + ", wirelessLinkRate=" + wirelessLinkRate.getValue() + ", power=" + power.getValue() + ", hopPattern=" + hopPattern.getValue() + ", encryptionKey=" + encryptionKey.getValue() + ", encryptionMode=" + encryptionMode.getValue()
                + ", serialBaudrRate=" + serialBaudrRate.getValue() + ", serialDataFormat=" + serialDataFormat.getValue() + ", serialChannelMode=" + serialChannelMode.getValue() + '}';

        
    }
}
