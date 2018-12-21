/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.pico900;

/**
 *
 * @author user
 */
public enum P900ErrorCode {
    SUCCESS((byte)0),
    SIZE((byte)1),
    LOGIN((byte)2),
    UNKNOWN_COMMAND((byte)0x10),
    LOCKED_OUT((byte)0x16);
    
    private byte id;
    
    P900ErrorCode(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }        
}
