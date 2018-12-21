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
public enum P900GeneralFunction {
    FLASH_UPGRADE(1),
    CLEAR_STATISTICS(2),
    SAVE_PROXY_PARAMETERS(3),
    MODEM_RESET(4),
    RSSI_REPORT(5),
    HOP_PATTER_INFO(6);
    
    private byte id;
    P900GeneralFunction(int id) {
        this.id = (byte)id;
    }

    public byte getId() {
        return id;
    }
        
}
