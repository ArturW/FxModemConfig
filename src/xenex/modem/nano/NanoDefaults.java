/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.nano;

/**
 *
 * @author user
 */
public enum NanoDefaults {    
    PMP_MASTER("PMP Master", 1),
    PMP_SLAVE("PMP Slave", 2),
    PMP_REPEATER("PMP Repeater", 3),
    PMP_MASTER_SLOW("PMP Master Slow", 4),
    PMP_REMOTE_SLOW("PMP Remote Slow", 5),
    PP_MASTER("PP Master", 6),
    PP_SLAVE("PP Slave", 7),
    PP_MASTER_SLOW("PP Master Slow", 8),
    PP_SLAVE_SLOW("PP Slave Slow", 9);
    
    
    private final String modeName;
    private final byte mode;
    
    NanoDefaults(String modeName, int mode) {
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
