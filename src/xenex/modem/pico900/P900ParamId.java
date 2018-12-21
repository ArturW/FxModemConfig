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
public enum P900ParamId {
    CHANNEL_MODE(1, 1),
    BAUD_RATE(2, 1),
    POWER(3, 1),
    POWER_NOW(29, 1),
    HOP_TIME(4, 1),
    VERSION_STRING(5, 0), //var    
    REPEATERS(10, 1),
    OPERATION_MODE(13, 1),
    LINK_RATE(14, 1),
    DESTINATION_ADDRESS(16, 6),
    DATA_FORMAT(19, 1),
    
    NETWORK_ADDRESS(25, 4),
    ROAMING_ADDRESS(28, 2),
    
    FEC(41, 1),
    NETWORK_TYPE(42, 1),
    USER(53, 32),
    COUNTRY_CODE(54, 1),
    ENCRYPTION_MODE(59, 1),
    HOP_PATTERN(64, 1),
    MAC_ADDRESS(79, 6), 
    UNIT_ADDRESS(80, 2),
    VERBOSE_MODE(83, 1),
    FACTORY_DEFAULTS(85, 1),
    PRODUCT_STRING(104, 0);
    
    
    private int id;
    private int size;
    
    private P900ParamId(int id, int size) {
        this.id=id;
        this.size = size;
    }

    public byte getId() {
        return (byte)id;
    }
    
    public int getSize() {
        return size;
    }
    
}
