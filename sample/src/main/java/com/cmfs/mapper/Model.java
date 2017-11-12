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
}
