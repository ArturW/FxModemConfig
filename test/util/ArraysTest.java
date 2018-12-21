/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import xenex.util.ArrayUtils;

/**
 *
 * @author user
 */
public class ArraysTest {
    
    public ArraysTest() {
    }
    
    @Test
    public void testToHexString() {
        byte[] bytes = new byte[0];
        String expected = "[]";
        String result = ArrayUtils.toHexString(bytes);
        Assert.assertEquals("Incorrect hex string", expected, result);
        
        bytes = new byte[] {0x00};
        expected = "[0x1]";        
        result = ArrayUtils.toHexString(bytes);
        Assert.assertNotEquals("Incorrect hex string", expected, result);
        
        bytes = new byte[] {72, 101, 108, 111};
        expected = "[0x48, 0x65, 0x6C, 0x6F]";        
        result = ArrayUtils.toHexString(bytes);
        Assert.assertEquals("Incorrect hex string", expected, result);
        
        bytes = new byte[] {0x03, 0x00};
        expected = "[0x03, 0x00]";        
        result = ArrayUtils.toHexString(bytes);
        Assert.assertEquals("Incorrect hex string", expected, result);
                
        bytes = new byte[] {(byte)0xF3, (byte)0xF0};
        expected = "[0xF3, 0xF0]";        
        result = ArrayUtils.toHexString(bytes);
        Assert.assertEquals("Incorrect hex string", expected, result);
        
        String hex = ArrayUtils.toHexString("771582391", ", ");
        System.out.println(hex);
    }
    
    
    
    @Test
    public void testNullConcat() {               
        final byte[] b1 = new byte[] {0x00, 0x01, 0x02};
        final byte[] b2 = null;
        final byte[] bytes = ArrayUtils.concat(b1, b2);
        
        final byte[] expected = new byte[] {0x00, 0x01, 0x02};
        Assert.assertArrayEquals(expected, bytes);
    }
    
    @Test
    public void testEmptyConcat() {               
        final byte[] b1 = new byte[] {0x00, 0x01, 0x02};
        final byte[] b2 = new byte[] {};        
        final byte[] bytes = ArrayUtils.concat(b1, b2);
        
        final byte[] expected = new byte[] {0x00, 0x01, 0x02};
        Assert.assertArrayEquals(expected, bytes);
    }
        
    @Test
    public void testConcat() {               
        final byte[] b1 = new byte[] {0x00, 0x01, 0x02};
        final byte[] b2 = new byte[] {0x03, 0x04, 0x05, 0x06, 0x07};
        
        final byte[] bytes = ArrayUtils.concat(b1, b2);
        
        final byte[] expected = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        Assert.assertArrayEquals(expected, bytes);                
    }
    
    @Test
    public void testMultiConcat() {
        final byte[] b1 = new byte[] {0x00, 0x01, 0x02};
        final byte[] b2 = new byte[] {0x03, 0x04, 0x05, 0x06, 0x07};
        final byte[] b3 = new byte[] {0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        
        final byte[] bytes = ArrayUtils.concat(b1, b2, b3);
        
        final byte[] expected = new byte[] {0x00, 0x01, 0x02,
            0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        
        Assert.assertArrayEquals(expected, bytes);        
    }
    
    
}
