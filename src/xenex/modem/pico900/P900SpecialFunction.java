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
public enum P900SpecialFunction {
    LOGIN(1);
        
    private byte id;
    
    P900SpecialFunction(int id) {
        this.id = (byte)id;
    }

    public byte getId() {
        return id;
    }
        
}
