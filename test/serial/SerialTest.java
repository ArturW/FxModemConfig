/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serial;

import java.nio.ByteBuffer;
import java.util.List;
import jssc.SerialPortException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import xenex.serial.Serial;
import xenex.util.CRC16;

/**
 *
 * @author user
 */
public class SerialTest {
    
    /* replace this with mock object */
    private Serial port;
    
    public SerialTest() {        
    }
    
    @Before
    public void setUp() throws SerialPortException {
        List<String> ports = Serial.getPortNames();
        String portName = ports.get(ports.size() - 1);
        port = Serial.newInstance(portName);
    }
    
    @After
    public void tearDown() {
        port.disconnect();
    }
    
    @Test
    public void testCRC() {
        byte[] data = {0x0b,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x12, 0x34,
            0x02,
            0x00,
            0x02};
        
        int crc = CRC16.getModbusValue(data);
        
        byte[] expected = new byte[] {0x00, 0x00, (byte)0xeb, (byte)0x17};
        int crcExpected = ByteBuffer.wrap(expected).getInt();
        
        Assert.assertEquals("Incorrect crc", crcExpected, crc);                
    }
    
    @Test
    public void testCRCCheck() {
        byte[] data = {0x0b,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x12, 0x34,
            0x02,
            0x00,
            0x02};
                        
        byte[] crc = new byte[] {(byte)0xeb, (byte)0x17};
        
        boolean result = port.checkCRC(data, crc);        
        Assert.assertTrue("Incorrect crc", result);
                
    }
}
