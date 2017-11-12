package com.cmfs.mapper;

import com.cmfs.mapper.annotations.ClassMapper;
import com.cmfs.mapper.annotations.FieldMapper;

/**
 * @author cmfs
 */

@ClassMapper("com.cmfs.mapper.TargetModel")
public class Model {

    @FieldMapper("str")
    private String strValue;

    @FieldMapper("i")
    private int intValue;

    @FieldMapper("i2")
    private int intValue2;

    private char charValue;

    private float floatValue;

    private double doubleValue;

    private long longValue;

    private short shortValue;

    private CharSequence charSequence;

    private byte byteValue;

    private boolean booleanValue;

    @FieldMapper("str")
    private TargetModel targetModel;

    public int getIntValue2() {
        return intValue2;
    }

    public void setIntValue2(int intValue2) {
        this.intValue2 = intValue2;
    }

    public short getShortValue() {
        return shortValue;
    }

    public void setShortValue(short shortValue) {
        this.shortValue = shortValue;
    }

    public CharSequence getCharSequence() {
        return charSequence;
    }

    public void setCharSequence(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public byte getByteValue() {
        return byteValue;
    }

    public void setByteValue(byte byteValue) {
        this.byteValue = byteValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public TargetModel getTargetModel() {
        return targetModel;
    }

    public void setTargetModel(TargetModel targetModel) {
        this.targetModel = targetModel;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public char getCharValue() {
        return charValue;
    }

    public void setCharValue(char charValue) {
        this.charValue = charValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }
}
