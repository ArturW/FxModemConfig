/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem;

import java.util.Objects;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 *
 * @author user
 */
public abstract class Modem {
    
    private final ReadOnlyProperty<String>  serialNumber;
            
    public Modem(String serialNumber) {
        //ReadOnlyJavaBeanStringProperty
        this.serialNumber =  new ReadOnlyStringWrapper(serialNumber);
    }

    public ReadOnlyProperty<String> serialNumber() {
        return serialNumber;
    }
    
    public String getSerialNumber() {
        return serialNumber.getValue();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.serialNumber);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Modem other = (Modem) obj;
        if (!Objects.equals(this.serialNumber, other.serialNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modem{" + "serialNumber=" + serialNumber.getValue() + '}';
    }
    
    
}
