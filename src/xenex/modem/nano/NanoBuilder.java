/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.nano;

public class NanoBuilder {

    private final String serialNumber;    
    private final String product;
    private final String countryCode;
    private final String firmware;
    private String modeOfOperation;
    private String networkAddress;
    private String unitAddress;
    private String destinationAddress;
    private String roamingAddress;
    private String wirelessLinkRate;
    private String power;
    private String hopPattern;
    private String encryptionKey;
    private String encryptionMode;
    private String serialBaudrRate;
    private String serialDataFormat;
    private String serialChannelMode;    
    
    public NanoBuilder (String serialNumber, String product, String countryCode, String firmware) {
        this.serialNumber = serialNumber;        
        this.product = product;
        this.countryCode = countryCode;
        this.firmware = firmware;
    }

    public NanoBuilder setModeOfOperation(String modeOfOperation) {
        this.modeOfOperation = modeOfOperation;
        return this;
    }

    public NanoBuilder setNetworkAddress(String networkAddress) {
        this.networkAddress = networkAddress;
        return this;
    }

    public NanoBuilder setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
        return this;
    }

    public NanoBuilder setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
        return this;
    }

    public NanoBuilder setRoamingAddress(String roamingAddress) {
        this.roamingAddress = roamingAddress;
        return this;
    }

    public NanoBuilder setWirelessLinkRate(String wirelessLinkRate) {
        this.wirelessLinkRate = wirelessLinkRate;
        return this;
    }

    public NanoBuilder setPower(String power) {
        this.power = power;
        return this;
    }

    public NanoBuilder setHopPattern(String hopPattern) {
        this.hopPattern = hopPattern;
        return this;
    }

    public NanoBuilder setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
        return this;
    }

    public NanoBuilder setEncryptionMode(String encryptionMode) {
        this.encryptionMode = encryptionMode;
        return this;
    }

    public NanoBuilder setSerialBaudrRate(String serialBaudrRate) {
        this.serialBaudrRate = serialBaudrRate;
        return this;
    }

    public NanoBuilder setSerialDataFormat(String serialDataFormat) {
        this.serialDataFormat = serialDataFormat;
        return this;
    }

    public NanoBuilder setSerialChannelMode(String serialChannelMode) {
        this.serialChannelMode = serialChannelMode;
        return this;
    }
    
    public Nano create() {
        return new Nano(product, countryCode, firmware, modeOfOperation, networkAddress, unitAddress, destinationAddress, roamingAddress, wirelessLinkRate, power, hopPattern, encryptionKey, encryptionMode, serialBaudrRate, serialDataFormat, serialChannelMode, serialNumber);
    }
    
}
