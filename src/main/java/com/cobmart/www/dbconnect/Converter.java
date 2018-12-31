package com.cobmart.www.dbconnect;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by HP on 4/25/2018.
 */

public class Converter {

    @TypeConverter
    public Date fromTimeStamp(Long value){
        return value == null ? null : new Date( value );
    }

    @TypeConverter
    public Long dateToTimeStamp(Date date){
        return  date == null ? null : date.getTime();
    }

}
