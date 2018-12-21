/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import xenex.util.ArrayUtils;
import xenex.util.CRC16;

/**
 *
 * @author user
 */
public class CRC16Test {
    
    public CRC16Test() {
    }

    //@Test
    public void testCRC() {
                
        byte[] data = {0x0b,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x12, 0x34,
            0x02,
            0x00,
            0x02};
            
        int crc= CRC16.getModbusValue(data);        
        System.out.println(crc);
        
        String crcString = Integer.toHexString(crc);        
        //int crcInteger1 = Integer.parseInt(crcString, 16);
        //Integer crcInteger2 = Integer.valueOf(crcString, 16);
        
        byte[] expectedBytes = new byte[] {0x00, 0x00, (byte)0xEB, 0x17};
        int expectedCRC = ByteBuffer.wrap(expectedBytes).getInt();                
        System.out.println(expectedCRC);                
        Assert.assertEquals("Incorrect crc", expectedCRC, crc);
                
    }
    
    @Test
    public void testPacketCRC() {
        byte[] crcExpected = new byte[] {(byte)0xF3, (byte)0xF0};
        final byte[] bytes = new byte[] 
            {0x0C,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
            0x12, 0x34,
            0x00,
            0x00, 0x02, 0x07,
            };//0xF3, 0xF0};
                
        int crcValue = CRC16.getModbusValue(bytes);
        byte[] array = ByteBuffer.allocate(4).putInt(crcValue).array();
        byte[] crc = Arrays.copyOfRange(array, 2,4);
        
        Assert.assertArrayEquals("Incorrect crc", crcExpected, crc);
        
    }    
}
