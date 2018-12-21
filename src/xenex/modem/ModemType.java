/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem;

/**
 *
 * @author user
 */
public enum ModemType {
    NANO("n920"), P900("p900");
    
    String type;
    
    ModemType(String type) {
        this.type = type;
    }
}
