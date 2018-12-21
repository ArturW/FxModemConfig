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
public enum NanoParamId {
    CHANNEL_MODE(1),
    BAUD_RATE(2),
    POWER(3),
    OPERATION_MODE(13),
    LINK_RATE(14),
    DESTINATION_ADDRESS_H(16),
    DESTINATION_ADDRESS_L(17),
    DATA_FORMAT(19),
    
    NETWORK_ID_H(22),
    NETWORK_ID_M2(23),
    NETWORK_ID_M1(24),
    NETWORK_ID_L(25),
    UNIT_ADDRESS_H(26),
    UNIT_ADDRESS_L(27),
    ROAMING_ADDRESS_H(30),
    ROAMING_ADDRESS_L(31),
    
    COUNTRY_CODE(89);
    
    byte id;
    
    private NanoParamId(int id) {
        this.id=(byte)id;
    }

    public byte getId() {
        return id;
    }        
    
}
