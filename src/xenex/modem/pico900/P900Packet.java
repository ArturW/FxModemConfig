/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.pico900;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import xenex.util.ArrayUtils;
import xenex.util.CRC16;

/**
 *
 * @author user
 */
public class P900Packet {        
    private final int HEADER_SIZE = 9;
    
    private final byte[] size; //1 contains mac, magic, control, payload
    private final byte[] mac;  //6
    private final byte[] magic; //2, can be used as sequence number
    private final byte[] control; //1
    private final byte[] payload; //x
    private final byte[] crc; //2 (size, mac, control, payload), not included in size
    
    P900Packet(byte[] mac, byte[] magic, byte[] control,
            byte[] payload) throws Exception {
            
        final int payloadSize = payload.length;
        final int packetSize = HEADER_SIZE + payloadSize;
        if (packetSize > 255) {
            throw new RuntimeException("Invalid packet size");
        }        
        //int = ByteBuffer.wrap(buffer).getInt();        
        this.size = ByteBuffer.allocate(1).put((byte)packetSize).array();
        this.mac = mac;
        this.magic = magic;
        this.control = control;
        this.payload = payload;
                                
        final byte []bytes = ArrayUtils.concat(size, mac, magic, control, payload);
        int crcValue = CRC16.getModbusValue(bytes);
        this.crc = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(crcValue).array(), 2,4);
    }

    public byte[] getSize() {
        return size;
    }

    public byte[] getMac() {
        return mac;
    }

    public byte[] getMagic() {
        return magic;
    }

    public byte[] getControl() {
        return control;
    }

    public byte[] getPayload() {
        return payload;
    }

    public byte[] getCrc() {
        return crc;
    }   
    
    @Override
    public String toString() {
        return "P900PacketStructure{" + "size=" + ArrayUtils.toHexString(size)
                + ", mac=" + ArrayUtils.toHexString(mac)
                + ", magic=" + ArrayUtils.toHexString(magic)
                + ", control=" + ArrayUtils.toHexString(control)
                + ", payload=" + ArrayUtils.toHexString(payload)
                + ", crc=" + ArrayUtils.toHexString(crc) + '}';
    }
    
    /*
    * Return packet as byte[]
    */    
    public byte[] getBytes() {
        return ArrayUtils.concat(size, mac, magic, control, payload, crc);
    }
    
    /*
    * Return packet as hex string
    */
    public String toHexString() {
        final byte[] packet = ArrayUtils.concat(size, mac, magic, control, payload, crc);
        return ArrayUtils.toHexString(packet);
    }
    
    /* 
    * Return payload without Cmd byte
    */
    public byte[] getData() {
        return Arrays.copyOfRange(payload, 1, payload.length);
    }
    
    public String getMacAddress (byte data[]) {
        final StringBuilder string = new StringBuilder();
        if (data.length < 6)
            return ":::::";
        for (int i = 0, count = 1; i < 6; i++) {                                                
            String hex = Integer.toHexString(data[i] & 0xFF);
            if (hex.length() == 1) {
                string.append("0");
            }            
            string.append(hex.toUpperCase());
            if (count < 6) {
                string.append(':');
                ++count;
            }
        }
        return new String(string);   
    }
    
    private int findPos(final byte[] bytes) {        
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == (byte)0x00)
                return i;
        }
        return -1;                
    }
    /*
    * Return payload withouot Cmd byte as String
    */
    public String getDataAsString(final byte[] data) {        
        if (data.length == 1)
            return "";        
        
        int endOfString = findPos(data);        
        String result = IntStream.range(1, endOfString)
                .mapToObj(i -> data[i])
                .map(each -> "" + (char)each.byteValue())
                .collect(Collectors.joining());
        
        return result;
    }      
    
    /*
    * Return payload withouot Cmd byte as String
    */
    public String getDataAsString() {
        final byte[] data = getData();
        //final P900ParamId paramId = findParamId(data);
        
        return getDataAsString(data);
    }

    /*
    * Return requested single paramter
    */
    public int returnSingleParameter(P900ParamId id) {
        final byte[] data = getData();        
        final int length = id.getSize();
        final byte[] param = new byte[length];        
        IntStream.range(0, length)
                .forEach(i -> param[i] = data[i + 1]);        
                
        final byte[] reverse = ArrayUtils.reverseOrder(param);
        // only for fix arrays
        //final int result = ByteBuffer.wrap(new byte[] {0, 0, param[1], param[0]}).getInt();       
        final BigInteger bigInt = new BigInteger(reverse);
        final int result = bigInt.intValue();        
        return result;
    }
    
    /*
    * Return requested single paramter
    */
    public int returnSingleParameter(P900ParamId id, byte[] data) {        
        final int length = id.getSize();
        final byte[] param = new byte[length];        
        IntStream.range(0, length)
                .forEach(i -> param[i] = data[i + 1]);        
                
        final byte[] reverse = ArrayUtils.reverseOrder(param);
        // only for fix arrays
        //final int result = ByteBuffer.wrap(new byte[] {0, 0, param[1], param[0]}).getInt();       
        final BigInteger bigInt = new BigInteger(reverse);
        final int result = bigInt.intValue();        
        return result;
    }
    
    
    /*
    * Return single paramter request
    */
    public int returnSingleParameter() {
        final byte[] data = getData();
        final P900ParamId paramId = findParamId(data);
        return returnSingleParameter(paramId);        
    }
    
    /*
    * Return single paramter request
    */
    public int returnSingleParameter(byte[] data) {        
        final P900ParamId paramId = findParamId(data);
        return returnSingleParameter(paramId, data);        
    }
    
    /*
    * Return requested single paramter
    */
    public byte[] returnSingleByteParameter(P900ParamId id) {
        final byte[] data = getData();        
        final int length = id.getSize();
        
        final byte[] param = new byte[length];
        IntStream.range(0, length)
                .forEach(i -> param[i] = data[i + 1]);
        final byte[] reverse = ArrayUtils.reverseOrder(param);
        return reverse;
    }        
    
    /*
    * Return requested single paramter
    */
    public byte[] returnSingleByteParameter(P900ParamId id, byte[] data) {        
        final int length = id.getSize();
        
        final byte[] param = new byte[length];
        IntStream.range(0, length)
                .forEach(i -> param[i] = data[i + 1]);
        final byte[] reverse = ArrayUtils.reverseOrder(param);
        return reverse;
    } 
    
    /*
    * Return single paramter request
    */
    public byte[] returnSingleByteParameter() {
        final byte[] data = getData();
        final P900ParamId paramId = findParamId(data);
        return returnSingleByteParameter(paramId);        
    }
    
    /*
    * Return single paramter request
    */
    public byte[] returnSingleByteParameter(byte[] data) {        
        final P900ParamId paramId = findParamId(data);
        return returnSingleByteParameter(paramId, data);        
    }
    
    public P900ParamId findParamId() {
        return findParamId(getData()); 
    }
    
    public P900ParamId findParamId(final byte[] data) {        
        final int id = data[0];                
        P900ParamId paramId = Stream.of(P900ParamId.values())
                .filter(each -> each.getId() == id)
                .findFirst()
                .get();
        return paramId;
    }
    
    
    // Domain 
    private boolean isValidID() {
        final byte invalidId = 8;
        final byte id = ByteBuffer
                .wrap(this.control)
                .get();
        
        if (invalidId == id)
            return false;
        return true;
    }
        
    
}
