/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modem.pico900;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import xenex.modem.pico900.P900Packet;
import xenex.modem.pico900.P900PacketBuilder;
import xenex.modem.pico900.P900ParamId;

/**
 *
 * @author user
 */
public class P900PacketTest {
    
    private final P900Packet rxPacket;
    
    private final byte[] expectedBytes = new byte[] 
        {0x0C,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
        0x12, 0x34,
        0x00,
        0x00, 0x02, 0x07,
        (byte)0xF3, (byte)0xF0};
    
    final byte[] size = new byte[] {0x0C};
    final byte[] mac = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x01};
    final byte[] magic = new byte[] {0x12, 0x34};
    final byte[] control = new byte[] {0x00};
    final byte[] payload = new byte[] {0x00, 0x02, 0x07};
    final byte[] crc = new byte[] {(byte)0xF3, (byte)0xF0};
    
    public P900PacketTest() {
                            
        rxPacket = new P900PacketBuilder()
                .setMac(mac)
                .setMagic(magic)
                .setControl(control)
                .setPayload(payload)
                .create();
        
        System.out.println(rxPacket);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testPacket() {
        byte[] rxSize = rxPacket.getSize();
        byte[] rxMac = rxPacket.getMac();
        byte[] rxMagic = rxPacket.getMagic();
        byte[] rxControl = rxPacket.getControl();
        byte[] rxPayload = rxPacket.getPayload();
        byte[] rxCrc = rxPacket.getCrc();
        
        Assert.assertArrayEquals(size, rxSize);
        Assert.assertArrayEquals(mac, rxMac);
        Assert.assertArrayEquals(magic, rxMagic);
        Assert.assertArrayEquals(control, rxControl);
        Assert.assertArrayEquals(payload, rxPayload);                    
        Assert.assertArrayEquals(crc, rxCrc);
    }
    @Test
    public void testGetBytes() {
        byte[] bytes = rxPacket.getBytes();        
        Assert.assertArrayEquals(expectedBytes, bytes);
    }
    
    @Test
    public void testReturnSingleParameterWithParamID() {
        byte[] expected = new byte[] {0x07};
        
        P900ParamId id = P900ParamId.BAUD_RATE;        
        int parameter = rxPacket.returnSingleParameter(id);
        byte[] actual = new byte[] {(byte)parameter};              
        Assert.assertArrayEquals(expected, actual);                
    }
        
    @Test
    public void testReturnSingleParameter() {
        byte[] expected = new byte[] {0x07};
        
        int parameter = rxPacket.returnSingleParameter();        
        byte[] actual = new byte[] {(byte)parameter};              
        Assert.assertArrayEquals(expected, actual);     
    }
        
    /* Test private method using reflections */
    @Test
    public void testFindParamId()
            throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        byte[] data = rxPacket.getData();
                
        Method method = P900Packet.class.getDeclaredMethod("findParamId", byte[].class);
        method.setAccessible(true);
        Object ob = method.invoke(rxPacket, data);
        P900ParamId id = (P900ParamId)ob;
        
        P900ParamId idExpected = P900ParamId.BAUD_RATE;
        Assert.assertEquals("Incorrect ParamId", idExpected , id);        
    }
        
    
}
