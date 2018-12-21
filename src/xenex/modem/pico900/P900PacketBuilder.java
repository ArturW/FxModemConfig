/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.pico900;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import xenex.util.ArrayUtils;


public class P900PacketBuilder {

    private final byte[] readCommand = new byte[] {0x00};
    private final byte[] writeCommand = new byte[] {0x04};    
    private final byte[] functionCommand = new byte[] {0x08};
    private final byte[] specialFunctionCommand = new byte[]{0x0C};
    
    private byte[] mac;
    private byte[] magic;
    private byte[] control;
    private byte[] payload;        
    
    public P900PacketBuilder() {
        this.mac = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        this.magic = new byte[] {(byte)0x12, (byte)0x34};
        this.control = new byte[] {(byte)0x02};
        this.payload = new byte[0];        
    }
    
    public P900PacketBuilder setMac(byte[] mac) {
        this.mac = mac;
        return this;
    }
    public P900PacketBuilder setMagic(byte[] magic) {
        this.magic = magic;
        return this;
    }

    public P900PacketBuilder setControl(byte[] control) {
        this.control = control;
        return this;
    }
        
    /* Includes command ID and data */
    public P900PacketBuilder setPayload(byte[] payload) {
        this.payload = payload;
        return this;
    }

    public P900PacketBuilder setRead(byte[] data) {
        this.payload = ArrayUtils.concat(readCommand, data);
        return this;
    }
    
    public P900PacketBuilder setRead(int d) {
        BigInteger integer = BigInteger.valueOf(d);
        byte [] data = integer.toByteArray();
        this.payload = ArrayUtils.concat(readCommand, data);
        return this;
    }
    
    public P900PacketBuilder setWrite(byte[] data) {
        this.payload = ArrayUtils.concat(writeCommand, data);
        return this;
    }
    
    public P900PacketBuilder setWrite(int d) {
        BigInteger integer = BigInteger.valueOf(d);
        byte [] data = integer.toByteArray();
        this.payload = ArrayUtils.concat(writeCommand, data);
        return this;
    }
     
    public P900PacketBuilder setGeneralFunction(byte[] data) {
        this.payload = ArrayUtils.concat(functionCommand, data);
        return this;
    }
    
    public P900PacketBuilder setSpecialFunction(byte[] data) {
        this.payload = ArrayUtils.concat(specialFunctionCommand, data);
        return this;
    }
    
    public P900Packet create() {
        try {            
            return new P900Packet(mac, magic, control, payload);
        } catch (Exception ex) {
            Logger.getLogger(P900PacketBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
