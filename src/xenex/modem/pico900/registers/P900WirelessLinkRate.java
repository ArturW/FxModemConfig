/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.pico900.registers;

/**
 *
 * @author user
 */
public enum P900WirelessLinkRate {
    _0(0, "172800"),
    _1(1, "2300400"),
    _2(2, "276480"),
    _3(3, "57600"),
    _4(4, "115200");
    
    byte param;
    String value;
    
    P900WirelessLinkRate(int param, String value) {
        this.param = (byte)param;
        this.value = value;
    }
    
}
