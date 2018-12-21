/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class ArrayUtils {
    
    public static String toHexString (String value, String separator) {
        return toHexString(value.getBytes(), separator);
    }

    public static String toHexString (byte[] value) {
        return toHexString(value, ", ");
    }
    public static String toHexString (byte[] value, String separator) {
        if (value.length == 0)
            return "[]";
        StringBuilder hex = new StringBuilder();        
        for (byte number : value) {                      
            String str = Integer.toHexString(number & 0xFF);            
            if (str.isEmpty())
                return "";
            
            str = str.toUpperCase();            
            if (str.length() > 2) {
                str = str.substring(str.length() - 1);
            }
            if (str.length() == 1) {
                str = "0" + str;
            }        
            hex.append("0x");
            hex.append(str);
            hex.append(separator);
        }                     
        hex.delete(hex.length()-2, hex.length());        
        hex.insert(0, "[");
        hex.append("]");   
        return hex.toString();    
    }
        
    /*
    public static byte[] concat(final byte[] bytes1, final byte[] bytes2) {
        byte[] buffer = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, buffer, 0, bytes1.length);
        System.arraycopy(bytes2, 0, buffer, bytes1.length, bytes2.length);        
        
        return buffer;
    }
    */
    public static byte[] concat(final byte[] ... bytes ) {           
        if (bytes == null) {
            return new byte[0];
        }
        
        int size = 0;
        for (byte[] array : bytes) {
            if (array == null)
                array = new byte[0];
            size += array.length;
        }
        byte[] buffer = new byte[size];
        
        int index = 0;
        for (byte[] array : bytes) {
            if (array == null)
                array = new byte[0];
            System.arraycopy(array, 0, buffer, index, array.length);
            index += array.length;
        }                
        return buffer;
    }
        
    public static byte[] bigEndian(final byte[] data) {
        final ByteBuffer bb = ByteBuffer
                .wrap(data)
                .order(ByteOrder.BIG_ENDIAN);
        final byte[] bytes = new byte[data.length];
        
        for (int i = 0; bb.hasRemaining(); i++) {
            bytes[i] = bb.get();
            
        }
        return bytes;
    }
    
    public static byte[] littleEndian(final byte[] data) {
        final ByteBuffer bb = ByteBuffer
                .wrap(data)
                .order(ByteOrder.LITTLE_ENDIAN);
        final byte[] bytes = new byte[data.length];
        
        for (int i = 0; bb.hasRemaining(); i++) {
            bytes[i] = bb.get();
            
        }
        return bytes;
    }
    
    public static byte[] reverseOrder(final byte[] a) {        
        for (int i = 0, j = a.length - 1; i < j; i++, j--) {
            byte temp = a[i];
            a[i] = a[j];
            a[j] = temp;            
        }
        return a;
    }
    
    public static void main(String[] args) {
        
        final byte[] b1 = new byte[] {0x00, 0x01, 0x02};
        final byte[] b2 = new byte[] {0x03, 0x04, 0x05, 0x06, 0x07};
        
        final byte[] bytes = ArrayUtils.concat(b1, b2, b1, b2);
        
        System.out.println(Arrays.toString(bytes));
    }
    
}
