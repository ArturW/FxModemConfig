/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.serial;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import xenex.modem.nano.NanoCommandId;
import xenex.modem.nano.Nano;
import xenex.modem.nano.NanoBuilder;
import xenex.modem.nano.NanoParamId;
import xenex.modem.pico900.P900;
import xenex.modem.pico900.P900Builder;
import xenex.modem.pico900.P900Packet;
import xenex.modem.pico900.P900PacketBuilder;
import xenex.modem.pico900.P900ParamId;
import xenex.modem.pico900.P900GeneralFunction;
import xenex.modem.pico900.P900SpecialFunction;
import xenex.modem.view.P900Presenter;
import xenex.serial.exceptions.P900Exception;
import xenex.util.ArrayUtils;
import xenex.util.CRC16;

/**
 *
 * @author user
 */
public class Serial implements AutoCloseable {
    static private final long DELAY = 500;
    private final SerialPort port;
    //private final StringBuilder sb = new StringBuilder();
    private final List<Byte> rxList = new LinkedList<>();
    
    private Serial(String portName) throws SerialPortException {
        System.out.println("Opening port: " + portName);
        port = new SerialPort(portName);
        setup(port);
        
    }
    
    public static Serial newInstance(String portName) throws SerialPortException {
        return new Serial(portName);               
    }
    
    public static List<String> getPortNames() {
        return Stream.of(SerialPortList.getPortNames())
                .collect(Collectors.toList());
    }    
    
    @Override
    public void close() throws Exception {
       this.disconnect();
    }
    
    private void setup(SerialPort port) throws SerialPortException{

        if (port.openPort()) {
            port.setParams(SerialPort.BAUDRATE_115200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
            //port.setEventsMask(SerialPort.MASK_RXCHAR); 

            port.addEventListener(event -> {               
                try {
                    sleep(0);
                    //sb.append(port.readString(event.getEventValue()));                                            
                    byte[] raw = port.readBytes();                        
                    IntStream.range(0, raw.length)                                
                            .forEach(i -> rxList.add(raw[i]));                        
                } catch (SerialPortException ex) {
                    System.err.println(ex.getMethodName());
                    
                    //System.err.println(ex);
                    //ex.printStackTrace();
                }
            });
        }                            
    }

    public void send(byte[] buffer) {
        //System.out.println("-> " + toHexString(buffer, " "));
        try {
            port.writeBytes(buffer);
        } catch (SerialPortException ex) {
            System.err.println(ex);
        }
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void send(NanoCommandId command) {
        byte[] buffer = new byte[] {0x03, 0x00, 0x00, command.getId()};
        //System.out.println("-> " + toHexString(buffer, " "));
        try {
            port.writeBytes(buffer);
        } catch (SerialPortException ex) {
            System.err.println(ex);
        }
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void send(NanoCommandId command, byte data) {        
        byte[] buffer = new byte[] {0x04, 0x00, 0x00, command.getId(), data};
        //System.out.println("-> " + toHexString(buffer, " "));
        try {
            port.writeBytes(buffer);
        } catch (SerialPortException ex) {
            System.err.println(ex);
        }
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void send(NanoCommandId command, byte[] data) {
        byte size = (byte)data.length;
        int offset = 4;
        byte[] buffer = new byte[offset + size];//
        buffer[0] = 0x04;
        buffer[1] = 0x00;
        buffer[2] = 0x00;
        buffer[3] = command.getId();
        IntStream.range(offset, offset + size)
                .forEach(i -> buffer[i] = data[i - offset]);
                
        try {
            port.writeBytes(buffer);
        } catch (SerialPortException ex) {
            System.err.println(ex);
        }
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void send(NanoCommandId command, NanoParamId parameter) {
        byte[] buffer = new byte[] {0x04, 0x00, 0x00, command.getId(), parameter.getId()};
        //System.out.println("-> " + toHexString(buffer, " "));
        try {
            port.writeBytes(buffer);
        } catch (SerialPortException ex) {
            System.err.println(ex);
        }
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
     
    private String getResultString() {
        return getResultString(0);
    }

    private String getResultString(int offset) {
        //byte[] data = Arrays.copyOfRange(rxBytes, 6, rxBytes.length - 1);
        if (rxList.isEmpty())
            return "";        
        String result = IntStream.range(offset, rxList.size() - 1)
                .mapToObj(i -> rxList.get(i))
                .map(each -> "" + (char)each.byteValue())
                .collect(Collectors.joining());
                
        rxList.clear();          
        return result;
    }
        
    /*
    * Return byte[] containing response
    */
    private byte[] getResultBytes( ) {
        return getResultBytes(0);
    }
    
    private byte[] getResultBytes(int offset) {
        //byte[] data = Arrays.copyOfRange(rxBytes, 6, rxBytes.length - 1);
        if (rxList.isEmpty())
            return new byte[0];        
        
        final byte[] result = new byte[rxList.size() - offset];            
        for(int i = offset; i < rxList.size(); i++) {
            result[i - offset] = rxList.get(i);
        }        
        rxList.clear();        
        return result;
    }
    
    public Nano readNanoSettings() {
        final int offset = 4;
        
        send(NanoCommandId.SERIAL_NUMBER);
        final String serialNumber = getResultString(offset);
         
        send(NanoCommandId.PRODUCT_NAME);
        final String productName = getResultString(offset);                                       
        
        send(NanoCommandId.FIRMWARE);        
        final String firmware = getResultString(offset);
        
        send(NanoCommandId.PARAMETERS, NanoParamId.COUNTRY_CODE);        
        final String countryCode = getResultString(offset);      
        
        send(NanoCommandId.GROUP0);
        byte[] group0 = getResultBytes(offset);
        //System.out.println(Arrays.toString(group0));
        
        send(NanoCommandId.GROUP1);
        byte[] group1 = getResultBytes(offset);
        //System.out.println(Arrays.toString(group1));
                      
        Map<NanoParamId, String> settings = parseNanoBytes(group0, group1);                     
        
        byte[] networkBuffer = new byte[] {Byte.parseByte(settings.get(NanoParamId.NETWORK_ID_H)),
            Byte.parseByte(settings.get(NanoParamId.NETWORK_ID_M2)),
            Byte.parseByte(settings.get(NanoParamId.NETWORK_ID_M1)),
            Byte.parseByte(settings.get(NanoParamId.NETWORK_ID_L))};
        int networkAddress = ByteBuffer.wrap(networkBuffer).getInt();
        
        byte[] addressBuffer = new byte[] {0, 0, Byte.parseByte(settings.get(NanoParamId.UNIT_ADDRESS_H)),
            Byte.parseByte(settings.get(NanoParamId.UNIT_ADDRESS_L))};
        int unitAddress = ByteBuffer.wrap(addressBuffer).getInt();
        
        byte[] destinationBuffer = new byte[] {0, 0, Byte.parseByte(settings.get(NanoParamId.DESTINATION_ADDRESS_H)),
            Byte.parseByte(settings.get(NanoParamId.DESTINATION_ADDRESS_L))};
        int destinationAddress = ByteBuffer.wrap(destinationBuffer).getInt();
        
        byte[] roamingBuffer = new byte[] {0, 0, Byte.parseByte(settings.get(NanoParamId.ROAMING_ADDRESS_H)),
            Byte.parseByte(settings.get(NanoParamId.ROAMING_ADDRESS_L))};
        int roamingAddress = ByteBuffer.wrap(roamingBuffer).getInt();
        
        Nano nano = new NanoBuilder(serialNumber, productName, countryCode, firmware)
                .setModeOfOperation(settings.get(NanoParamId.OPERATION_MODE))
                .setNetworkAddress(String.valueOf(networkAddress))
                .setUnitAddress(String.valueOf(unitAddress))
                .setDestinationAddress(String.valueOf(destinationAddress))
                .setRoamingAddress(String.valueOf(roamingAddress))
                .setWirelessLinkRate(settings.get(NanoParamId.LINK_RATE))
                .setPower(settings.get(NanoParamId.POWER))
                // P900 only .setHopPattern(settings.get(NanoParamId.)
                //.setEncryptionKey(settings.get(NanoParamId.)
                .setSerialBaudrRate(settings.get(NanoParamId.BAUD_RATE))
                .setSerialDataFormat(settings.get(NanoParamId.DATA_FORMAT))
                .setSerialChannelMode(settings.get(NanoParamId.CHANNEL_MODE))
                .create();
               
        return nano;
    }
 
    private Map parseNanoBytes(byte[] group0, byte[] group1) {
        Map<NanoParamId, String> settings = new TreeMap<>();
        int group0_offset = 1;
        settings.put(NanoParamId.CHANNEL_MODE, String.valueOf(group0[NanoParamId.CHANNEL_MODE.getId() * 2 - group0_offset] & 0xFF));
        settings.put(NanoParamId.BAUD_RATE, String.valueOf(group0[NanoParamId.BAUD_RATE.getId() * 2 - group0_offset] & 0xFF));
        settings.put(NanoParamId.POWER, String.valueOf(group0[NanoParamId.POWER.getId() * 2 - group0_offset] & 0xFF));
        settings.put(NanoParamId.OPERATION_MODE, String.valueOf(group0[NanoParamId.OPERATION_MODE.getId() * 2 - group0_offset] & 0xFF));
        settings.put(NanoParamId.LINK_RATE, String.valueOf(group0[NanoParamId.LINK_RATE.getId() * 2 - group0_offset] & 0xFF));
        settings.put(NanoParamId.DESTINATION_ADDRESS_H, String.valueOf(group0[NanoParamId.DESTINATION_ADDRESS_H.getId() * 2 - group0_offset]));
        settings.put(NanoParamId.DESTINATION_ADDRESS_L, String.valueOf(group0[NanoParamId.DESTINATION_ADDRESS_L.getId() * 2 - group0_offset]));
        settings.put(NanoParamId.DATA_FORMAT, String.valueOf(group0[NanoParamId.DATA_FORMAT.getId() * 2 - group0_offset] & 0xFF));
        
        int group1_offset = 21;
        settings.put(NanoParamId.NETWORK_ID_H, String.valueOf(group1[(NanoParamId.NETWORK_ID_H.getId() - group1_offset) * 2 + 1]));
        settings.put(NanoParamId.NETWORK_ID_M2, String.valueOf(group1[(NanoParamId.NETWORK_ID_M2.getId() - group1_offset) * 2 + 1]));
        settings.put(NanoParamId.NETWORK_ID_M1, String.valueOf(group1[(NanoParamId.NETWORK_ID_M1.getId() -group1_offset) * 2 + 1]));
        settings.put(NanoParamId.NETWORK_ID_L, String.valueOf(group1[(NanoParamId.NETWORK_ID_L.getId() - group1_offset) * 2 + 1]));
        settings.put(NanoParamId.UNIT_ADDRESS_H, String.valueOf(group1[(NanoParamId.UNIT_ADDRESS_H.getId() - group1_offset) * 2 + 1]));
        settings.put(NanoParamId.UNIT_ADDRESS_L, String.valueOf(group1[(NanoParamId.UNIT_ADDRESS_L.getId() - group1_offset) * 2 + 1]));
        settings.put(NanoParamId.ROAMING_ADDRESS_H, String.valueOf(group1[(NanoParamId.ROAMING_ADDRESS_H.getId() - group1_offset) * 2 + 1]));
        settings.put(NanoParamId.ROAMING_ADDRESS_L, String.valueOf(group1[(NanoParamId.ROAMING_ADDRESS_L.getId() - group1_offset) * 2 + 1]));
        
        //System.out.println(settings);
        return settings;
    }        
    
    public P900 readP900Settings() throws P900Exception {
        System.out.println("readP900Settings");
        byte[] requestParameters = new byte[] {
            P900ParamId.MAC_ADDRESS.getId(),            
            P900ParamId.VERSION_STRING.getId(),
            P900ParamId.PRODUCT_STRING.getId(),
            P900ParamId.COUNTRY_CODE.getId(),
            P900ParamId.OPERATION_MODE.getId(),
            P900ParamId.NETWORK_ADDRESS.getId(),
            P900ParamId.UNIT_ADDRESS.getId(),
            P900ParamId.DESTINATION_ADDRESS.getId(),
            P900ParamId.ROAMING_ADDRESS.getId(),
            P900ParamId.LINK_RATE.getId(),
            P900ParamId.POWER.getId(),
            P900ParamId.HOP_PATTERN.getId(),
            P900ParamId.ENCRYPTION_MODE.getId(),
            P900ParamId.BAUD_RATE.getId(),
            P900ParamId.DATA_FORMAT.getId(),
            P900ParamId.CHANNEL_MODE.getId(),
        };
                
        P900Packet txPacket = new P900PacketBuilder()
                .setRead(requestParameters)
                .setMagic(new byte[] {0x12, 0x34})
                .create();
                        
        P900Packet rxPacket = this.send(txPacket);        
        Map<P900ParamId, String> parameters = parseReceivedPacket(rxPacket);
        //System.out.println(parameters);
        P900 modem = createModem(parameters);
        return modem;
    }        
    
    public P900Packet send(P900Packet txPacket) throws P900Exception {                
        final byte[] txBytes = txPacket.getBytes();
        System.out.println("-> " + txPacket.toHexString());
        this.send(txBytes);
                                
        // send contains delay specified by global DELAY value
        final byte[] rxBytes = this.getResultBytes();        
        // return empty packet if response is empty
        if (rxBytes.length == 0)
            return new P900PacketBuilder()
                    .setMac(txPacket.getMac())
                    .create();
        
        P900Packet rxPacket = parseP900Bytes(rxBytes);
        System.out.println("<- " + rxPacket.toHexString());
        return rxPacket;
    }        
    
    private P900Packet parseP900Bytes(byte[] bytes) throws P900Exception {        
        final int size = Array.getInt(bytes, 0);
        // System.arraycopy(buffer, size, buffer, size, size)
        final byte[] mac = Arrays.copyOfRange(bytes, 1, 7);
        final byte[] magic = Arrays.copyOfRange(bytes, 7, 9);
        final byte[] control = Arrays.copyOfRange(bytes, 9, 10);
        final byte[] payload = Arrays.copyOfRange(bytes, 10, bytes.length - 2);
        final byte[] crc = Arrays.copyOfRange(bytes, bytes.length - 2, bytes.length);
               
        final byte[] data = Arrays.copyOfRange(bytes, 0, bytes.length - 2);
        
        final boolean crcCheck = checkCRC(data, crc);
        if (crcCheck == false)
            throw new P900Exception("Invalid CRC\n"
                    + " data=" + ArrayUtils.toHexString(data)
                    + " crc=" + ArrayUtils.toHexString(crc) );
            
        final byte controlError = 0b0000_1000;        
        if (control[0] == controlError) {            
            throw new P900Exception("Control error=" + controlError + "\n"
                    + " errorCode=?"                    
                    + " packet=" + ArrayUtils.toHexString(bytes));
        }
        
        final P900Packet rxPacket = new P900PacketBuilder()
                .setMac(mac)
                .setMagic(magic)
                .setControl(control)
                .setPayload(payload)                
                .create();
        
        return rxPacket;
    }
    
    public boolean checkCRC(final byte[] data, final byte[]dataCrc) {        
        final int crc = CRC16.getModbusValue(data);       
        final int expectedCRC = ByteBuffer
                .wrap(new byte[] {0x00, 0x00, dataCrc[0], dataCrc[1]})
                .getInt();
                
        if (crc == expectedCRC)
            return true;
        return false;
    }            
       
    /*
    * Return map containging requested parameters
    */
    private Map<P900ParamId, String> parseReceivedPacket(P900Packet packet) {
       
        Map<P900ParamId, String> settings = new HashMap<>();
        int index;
        byte[] data = packet.getData();
        //System.out.println(ArrayUtils.toHexString(data));                
        P900ParamId param = packet.findParamId(data);
        byte[] bytes = packet.returnSingleByteParameter(param, data);
        String value = packet.getMacAddress(bytes);
        settings.put(param, value);
        index = param.getSize() + 1;
        data = Arrays.copyOfRange(data, index, data.length);
        
        //System.out.println(ArrayUtils.toHexString(data));        
        String ver = packet.getDataAsString(data);
        param = packet.findParamId(data);
        settings.put(param, ver);
        index = ver.length() + 2;        
        data = Arrays.copyOfRange(data, index, data.length);
        
        ver = packet.getDataAsString(data);
        param = packet.findParamId(data);
        settings.put(param, ver);
        index = ver.length() + 2;        
        data = Arrays.copyOfRange(data, index, data.length);
        
        final int items = 13;
        for (int i = 0; i< items; i++) {
            //System.out.println(ArrayUtils.toHexString(data));
            param = packet.findParamId(data);
            bytes = packet.returnSingleByteParameter(param, data);
            BigInteger integer = new BigInteger(bytes);
            value = String.valueOf(integer.intValue());
            settings.put(param, value);
            index = param.getSize() + 1;
            data = Arrays.copyOfRange(data, index, data.length);
        }                     
        return settings;
    }    
    
    private P900 createModem(Map<P900ParamId, String> params) {
    
        P900 modem = new P900Builder("",
                params.get(P900ParamId.MAC_ADDRESS),
                params.get(P900ParamId.PRODUCT_STRING),
                params.get(P900ParamId.VERSION_STRING),
                params.get(P900ParamId.COUNTRY_CODE))                
                .setModeOfOperation(params.get(P900ParamId.OPERATION_MODE))
                .setNetworkAddress(params.get(P900ParamId.NETWORK_ADDRESS))
                .setUnitAddress(params.get(P900ParamId.UNIT_ADDRESS))
                .setDestinationAddress(params.get(P900ParamId.DESTINATION_ADDRESS))
                .setRoamingAddress(params.get(P900ParamId.ROAMING_ADDRESS))
                .setWirelessLinkRate(params.get(P900ParamId.LINK_RATE))
                .setPower(params.get(P900ParamId.POWER))
                .setHopPattern(params.get(P900ParamId.HOP_PATTERN))
                .setEncryptionMode(params.get(P900ParamId.ENCRYPTION_MODE))
                .setSerialBaudrRate(params.get(P900ParamId.BAUD_RATE))
                .setSerialDataFormat(params.get(P900ParamId.DATA_FORMAT))
                .setSerialChannelMode(params.get(P900ParamId.DATA_FORMAT))
                .create();                                                
        return modem;
    }

    public P900Packet saveProxyParameters() throws P900Exception {
        System.out.println("saveProxySettings");
        byte [] payload = new byte[] {            
            P900GeneralFunction.SAVE_PROXY_PARAMETERS.getId()
        };
        
        P900Packet txPacket = new P900PacketBuilder()                
                .setGeneralFunction(payload)                    
                .create();
            
        P900Packet rxPacket = this.send(txPacket);
        return rxPacket;
    }        
    
    public P900Packet login(String pwd) throws P900Exception {
        System.out.println("login");
        
        byte [] function = new byte[] {P900SpecialFunction.LOGIN.getId()};
        byte [] parameter = new byte[] {0x00}; // function parameter - sending login information
        byte [] pwdAsBytes = pwd.getBytes();
        byte [] endOfString = new byte[] {0x00};
                        
        byte[] payload = ArrayUtils.concat(function, parameter, pwdAsBytes, endOfString);
        
        P900Packet txPacket = new P900PacketBuilder()                
                .setSpecialFunction(payload)                    
                .create();
            
        
        P900Packet rxPacket = this.send(txPacket);
        
        
        return rxPacket;
    }    
    
    public void disconnect(){
        if (port != null) {
            try {
                port.removeEventListener();
                if(port.isOpened()){
                    port.closePort();
                }
            } catch (SerialPortException ex) {
                System.err.println("ERROR closing port exception: " + ex.toString());
            }
            System.out.println("Closing port: " + port.getPortName());
        }
    }
            
    //result[5] = (byte) (value & 0xFF);           // Least significant "byte"
    //result[6] = (byte) ((value & 0xFF00) >> 8);  // Most significant "byte"
    
    //http://stackoverflow.com/questions/5167079/lsb-msb-handling-in-java
    
    public static void main(String[] args) throws InterruptedException, SerialPortException {
        
        System.out.println(Serial.getPortNames());
        String file = "/dev/ttyS5";
        
        Serial port = Serial.newInstance(file);
        
        //Nano nano = port.readNanoSettings();        
        //System.out.println(nano);
        
        
        //port.send(NanoCommandId.FIRMWARE);        
        //final String firmware = port.getResultString();
        //System.out.println(firmware);
        //port.disconnect();
        port.disconnect();
        
        System.out.println();
        file = "/dev/ttyUSB0";        
        port = Serial.newInstance(file);
        
        byte[] data = {0x0b,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x12, 0x34,
            0x02,
            0x00,
            0x02, 
            (byte)0xeb, (byte)0x17};
        
        
        //System.out.println(Arrays.toString(data));
        System.out.println("-> " + ArrayUtils.toHexString(data));
        port.send(data);
        //String str = port.getResultString();
        byte[] rx = port.getResultBytes();
        //System.out.println(Arrays.toString(rxBytes));
        System.out.println("<- " + ArrayUtils.toHexString(rx));
        
        port.send(data);
        //byte[] result = port.getResultBytes();
        //System.out.println(firmware);              
        
        port.disconnect(); // if port stays open its not purged
        System.out.println();  
        port = Serial.newInstance(file);
        
        //P900ParamId paramId = P900ParamId.VERSION_STRING;
        //data = new byte[] {(byte)paramId.getId()};
        data = new byte[] {
            //P900ParamId.MAC_ADDRESS.getId(),
            P900ParamId.PRODUCT_STRING.getId()
            //P900ParamId.VERSION_STRING.getId(),
            //P900ParamId.COUNTRY_CODE.getId(),
            //P900ParamId.OPERATION_MODE.getId()
        };
        P900Packet txPacket = new P900PacketBuilder()
                .setRead(data)
                .setMagic(new byte[] {0x12, 0x34})
                .create();
        
        System.out.println("-> " + txPacket.toHexString());
        //System.out.println(txPacket);                
        try {
            P900Packet rxPacket = port.send(txPacket);
            
            
            byte[] result = rxPacket.getData();
            System.out.println("r + " + ArrayUtils.toHexString(result));
            String p = rxPacket.getDataAsString();
            System.out.println(p);

            //P900 modem = port.readP900Settings();
            //System.out.println(modem);
        } catch (Exception e) {
            System.err.println(e);            
        } 

        port.disconnect();
        System.out.println();
       
        byte[] bytes = new byte[] {(byte)0x31, (byte)0x255};
        
        String str = String.valueOf(bytes[0] & 0xFF);
        
        System.out.println(str);
                                
    }
    
    private void sleep(long value) {
        try {
            Thread.sleep(value);
        } catch (InterruptedException ex) {
            Logger.getLogger(P900Presenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
