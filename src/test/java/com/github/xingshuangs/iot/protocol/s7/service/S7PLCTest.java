package com.github.xingshuangs.iot.protocol.s7.service;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

@Ignore
public class S7PLCTest {
    private S7PLC s7PLC = new S7PLC(EPlcType.S1200, "192.168.3.98");
//    private S7PLC s7PLC = new S7PLC(EPlcType.S200_SMART, "192.168.3.102");
//    private S7PLC s7PLC = new S7PLC(EPlcType.S1200);

    @Test
    public void readByte() {
//        byte data = s7PLC.readByte("DB14.0.0");
        byte[] actual = s7PLC.readByte("DB14.0.1", 2);
        assertEquals(2, actual.length);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            try {
                s7PLC.readBoolean("DB14.2.0");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void readBoolean() {
        List<Boolean> actual = s7PLC.readBoolean("DB1.2.0", "DB1.2.1", "DB1.2.7");
        assertEquals(3, actual.size());
        s7PLC.readBoolean("DB1.2.0");
    }


    @Test
    public void writeBoolean() {
        s7PLC.writeBoolean("DB2.0.7", false);
        boolean res = s7PLC.readBoolean("DB2.0.7");
        assertFalse(res);

        List<Boolean> actual = s7PLC.readBoolean("DB1.2.0", "DB1.2.1", "DB1.2.7");
        assertEquals(3, actual.size());
    }

    @Test
    public void writeByte() {
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        byte actual = s7PLC.readByte("DB2.1");
        assertEquals((byte) 0x11, actual);
    }

    @Test
    public void writeUInt16() {
        s7PLC.writeUInt16("DB2.0", 0x2222);
        int actual = s7PLC.readUInt16("DB2.0");
        assertEquals(0x2222, actual);
    }

    @Test
    public void writeInt16() {
        s7PLC.writeInt16("DB14.0", (short) 16);
        int i = s7PLC.readUInt16("DB14.0");
        assertEquals(16, i);

        List<Integer> actual = s7PLC.readUInt16("DB1.0", "DB1.2");
        assertEquals(2, actual.size());
    }

    @Test
    public void writeUInt32() {
        s7PLC.writeUInt32("DB2.0", 0x11111122);
        long actual = s7PLC.readUInt32("DB2.0");
        assertEquals(0x11111122L, actual);

        List<Long> actual1 = s7PLC.readUInt32("DB1.0", "DB1.4");
        assertEquals(2, actual1.size());
    }

    @Test
    public void writeInt32() {
        s7PLC.writeInt32("DB2.0", 0x11113322);
        int i = s7PLC.readInt32("DB2.0");
        assertEquals(0x11113322, i);
    }

    @Test
    public void writeFloat32() {
        s7PLC.writeFloat32("DB2.0", 12);
        float actual = s7PLC.readFloat32("DB2.0");
        assertEquals(12, actual, 0.00001);
    }

    @Test
    public void writeFloat64() {
        s7PLC.writeFloat64("DB2.0", 12.02);
        double actual = s7PLC.readFloat64("DB2.0");
        assertEquals(12.02, actual, 0.00001);
    }

    @Test
    public void writeString() {
        s7PLC.writeString("DB2.4", "demo");
        String str = s7PLC.readString("DB2.4");
        assertEquals("demo", str);
    }

    @Test
    public void writeMultiData() {
        MultiAddressWrite addressWrite = new MultiAddressWrite();
        addressWrite.addUInt16("DB14.0", (byte) 0x11)
                .addUInt16("DB14.4", 88)
                .addBoolean("DB14.2.0", true);
        s7PLC.writeMultiData(addressWrite);
        boolean actual = s7PLC.readBoolean("DB14.2.0");
        assertTrue(actual);

        MultiAddressRead addressRead = new MultiAddressRead();
        addressRead.addData("DB1.0", 1)
                .addData("DB1.2", 3)
                .addData("DB1.3", 5);
        List<byte[]> list = s7PLC.readMultiByte(addressRead);
        assertEquals(1, list.get(0).length);
        assertEquals(3, list.get(1).length);
        assertEquals(5, list.get(2).length);
    }

    @Test
    public void writeI() {
        s7PLC.writeBoolean("I0.5", true);

        List<Boolean> booleans = s7PLC.readBoolean("I0.0", "I0.1", "I0.2", "I0.3", "I0.4", "I0.5");
        assertEquals(6, booleans.size());
        System.out.println(booleans);

    }

    @Test
    public void writeQ() {
        s7PLC.writeBoolean("Q0.7", true);
        boolean b = s7PLC.readBoolean("Q0.7");
        assertTrue(b);

        List<Boolean> booleans = s7PLC.readBoolean("Q0.0", "Q0.1", "Q0.2", "Q0.3", "Q0.4", "Q0.5", "Q0.6", "Q0.7");
        System.out.println(booleans);
    }

    @Test
    public void writeM() {
        s7PLC.writeBoolean("M0.4", true);
        boolean b = s7PLC.readBoolean("M0.4");
        assertTrue(b);

        s7PLC.writeByte("M0", (byte) 0x33);
        byte m1 = s7PLC.readByte("M0");
        assertEquals((byte) 0x33, m1);

        List<Boolean> booleans = s7PLC.readBoolean("M1.0", "M1.1", "M1.2", "M1.3", "M1.4", "M1.5", "M1.6", "M1.7");
        System.out.println(booleans);
    }

    @Test
    public void writeV() {
        s7PLC.writeBoolean("V1.4", true);
        boolean b = s7PLC.readBoolean("V1.4");
        assertTrue(b);

        s7PLC.writeByte("V2", (byte) 0x34);
        byte v2 = s7PLC.readByte("V2");
        assertEquals((byte) 0x34, v2);
    }
}