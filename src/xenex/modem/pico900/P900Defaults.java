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
public enum P900Defaults {
    MESH_PRIMARY("Mesh Primary", 1),
    MESH_REMOTE("Mesh Remote", 2),
    MESH_SECONDARY("Mesh Secondary", 3),
    PMP_MASTER("PMP Master", 7),
    PMP_SLAVE("PMP Slave", 8),
    PMP_REPEATER("PMP Repeater", 9),
    PP_MASTER("PP Master", 10),
    PP_SLAVE("PP Slave", 11),
    PP_REPEATER("PP Repeater", 12);
        
    private final String modeName;
    private final byte mode;
    
    P900Defaults(String modeName, int mode) {
        this.modeName = modeName;
        this.mode = (byte)mode;
    }

    public String getModeName() {
        return modeName;
    }
    
    public byte getMode() {
        return mode;
    }
    
    
}
