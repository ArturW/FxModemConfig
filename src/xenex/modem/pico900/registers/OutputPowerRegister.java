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
public enum OutputPowerRegister {
    _20(20, "20db - 100mW"),
    _21(21, "21db - 125mW"),
    _22(22, "21db - 160mW"),
    _23(23, "21db - 200mW"),
    _24(24, "21db - 250mW"),
    _25(25, "21db - 320mW"),
    _26(26, "21db - 400mW"),
    _27(27, "21db - 500mW"),
    _28(28, "21db - 680mW"),
    _29(29, "21db - 800mW"),
    _30(30, "30db - 1000mW");

    byte param;
    String value;

    OutputPowerRegister(int param, String value) {
        this.param = (byte)param;
        this.value = value;
    }
    
}
