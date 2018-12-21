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
public enum NanoCommandId {
    
    /* Get */
    GROUP0(0),
    GROUP1(2),
    PARAMETERS(20),
    PARAMETERS2(21),
    FIRMWARE(40),
    SERIAL_NUMBER(41),
    ENCRYPION_KEY(42),
    DEFAULTS(47), //Example: 4, 0, 5, 47, 1
    MANUFACTURE_NUMBER(55),
    PRODUCT_NAME(57),
    FLASH(65),
    
    /* Set */
    SET_PARAMETERS(70),
    SET_ENCRYPION_KEY(81),
    REGISTER_REPEATERS(84),
    RESET(255),
    /* RESPONSE */
    GROUP_RESPONSE(100),
    PARAMETERS_RESPONSE(100),
    PARAMETERS_RESPONSE2(121),
    FIRMWARE_RESPONSE(140),
    SERIAL_NUMBER_RESPONSE(141),
    ENCRYPION_KEY_RESPONSE(142),
    MANUFACTURE_NUMBER_RESPONSE(155),
    PRODUCT_NAME_RESPONSE(157),
    FLASH_RESPONSE(165),
    REGISTER_REPEATERS_RESPONSE(184);
    
    byte id;
    
    private NanoCommandId(int id) {
        this.id = (byte)id;
    }
    
    public byte getId() {
        return id;
    }

    public static byte SERIAL_NUMBER() {
        return SERIAL_NUMBER.id;
    }
    
    public static byte PRODUCT_NAME() {
        return PRODUCT_NAME.id;
    }

}
