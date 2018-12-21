/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.pico900;


public class P900Builder {

    private final String serialNumber;
    private final String macAddress;
    private final String product;
    private final String firmware;
    private final String countryCode;
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
    
    public P900Builder (String serialNumber, String macAddress, String product, String firmware, String countryCode) {
        this.serialNumber = serialNumber;
        this.macAddress = macAddress;
        this.product = product;
        this.firmware = firmware;
        this.countryCode = countryCode;        
    }

    public P900Builder setModeOfOperation(String modeOfOperation) {
        this.modeOfOperation = modeOfOperation;
        return this;
    }

    public P900Builder setNetworkAddress(String networkAddress) {
        this.networkAddress = networkAddress;
        return this;
    }

    public P900Builder setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
        return this;
    }

    public P900Builder setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
        return this;
    }

    public P900Builder setRoamingAddress(String roamingAddress) {
        this.roamingAddress = roamingAddress;
        return this;
    }

    public P900Builder setWirelessLinkRate(String wirelessLinkRate) {
        this.wirelessLinkRate = wirelessLinkRate;
        return this;
    }

    public P900Builder setPower(String power) {
        this.power = power;
        return this;
    }

    public P900Builder setHopPattern(String hopPattern) {
        this.hopPattern = hopPattern;
        return this;
    }

    public P900Builder setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
        return this;
    }

    public P900Builder setEncryptionMode(String encryptionMode) {
        this.encryptionMode = encryptionMode;
        return this;
    }

    public P900Builder setSerialBaudrRate(String serialBaudrRate) {
        this.serialBaudrRate = serialBaudrRate;
        return this;
    }

    public P900Builder setSerialDataFormat(String serialDataFormat) {
        this.serialDataFormat = serialDataFormat;
        return this;
    }

    public P900Builder setSerialChannelMode(String serialChannelMode) {
        this.serialChannelMode = serialChannelMode;
        return this;
    }
    
    public P900 create() {
        return new P900(macAddress, product, firmware, countryCode, modeOfOperation, networkAddress, unitAddress, destinationAddress, roamingAddress, wirelessLinkRate, power, hopPattern, encryptionKey, encryptionMode, serialBaudrRate, serialDataFormat, serialChannelMode, serialNumber);
    }
    
}
